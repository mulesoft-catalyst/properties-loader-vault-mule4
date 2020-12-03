package com.xyzcompany.mulesoft.mule4;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ConfigClient {
	private String username;
	private String password;
	private String secretsUrl;
	private String nonSecretsUrl;
	private static Map<String, Map<String, String>> propertiesMap = new HashMap<String, Map<String, String>>();

	private static final Logger logger = LogManager.getLogger(ConfigClient.class.getName());

	public ConfigClient() {
		throw new RuntimeException("Spring Bean not properly configured");
	}

	public ConfigClient(String username, String password, String secretsUrl, String nonSecretsUrl) {
		this.username = username;
		this.password = password;
		this.secretsUrl = secretsUrl;
		this.nonSecretsUrl = nonSecretsUrl;
	}
	
	public ConfigClient(String username, String password, String secretsUrl) {
		this.username = username;
		this.password = password;
		this.secretsUrl = secretsUrl;
	}

	public Map<String, Map<String, String>> retrievePropertiesMap() throws Exception {

		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}

		} };

		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

		loadProperties("secrets");
		//loadProperties("non-secrets");

		return propertiesMap;
	}

	private void loadProperties(String type) throws Exception {
		StringBuilder response = new StringBuilder();
		String serviceUrl = (type == "secrets") ? this.secretsUrl : this.nonSecretsUrl;
		logger.info("Fetching " + type + ". Invoking Secrets Manager API at " + serviceUrl);

		try {
			String encodedCreds = Base64.getEncoder()
					.encodeToString((this.username + ":" + this.password).getBytes(StandardCharsets.UTF_8));
			URL url = new URL(String.format("%s", serviceUrl));
			HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
			urlConnection.setRequestProperty("Accept", "application/json");
			urlConnection.setRequestProperty("Authorization", "Basic " + encodedCreds);

			if (urlConnection.getResponseCode() != 200) {
				BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
				String line;
				while ((line = br.readLine()) != null) {
					response.append(line);
				}
				br.close();
				urlConnection.disconnect();
				throw new RuntimeException(
						"API call failed" + System.lineSeparator() + "Response code: " + urlConnection.getResponseCode()
								+ System.lineSeparator() + "Response: " + response.toString());
			}
			logger.info("Retreived " + type + " successully");
			BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
				response.append(line);
			}
			br.close();
			urlConnection.disconnect();
		} catch (Exception e) {
			throw new RuntimeException("Trouble retrieving " + type + " from Secrets Manager URL " + serviceUrl
					+ ". Exception message is: " + System.lineSeparator() + e.getMessage());
		}

		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<String, String>();
		if (response.length() > 0) {
			map = mapper.readValue(response.toString(), new TypeReference<Map<String, String>>() {
			});
			if (!map.isEmpty())
				propertiesMap.put(type, map);
		}
	}
}

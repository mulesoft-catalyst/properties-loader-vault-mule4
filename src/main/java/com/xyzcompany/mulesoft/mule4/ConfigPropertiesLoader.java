package com.xyzcompany.mulesoft.mule4;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigPropertiesLoader {
	private String username;
	private String password;
	private String secretsUrl;
	private String nonSecretsUrl;

	private static final Logger logger = LogManager.getLogger(ConfigPropertiesLoader.class.getName());

	public ConfigPropertiesLoader(String username, String password, String secretsUrl, String nonSecretsUrl) {
		this.username = username;
		this.password = password;
		this.secretsUrl = secretsUrl;
		this.nonSecretsUrl = nonSecretsUrl;
	}


	public Map<String, String> getProperties() throws Exception {
		ConfigClient vc = new ConfigClient(this.username, this.password, this.secretsUrl, this.nonSecretsUrl);
		byte[] resourceData = null;
		Map<String, Map<String, String>> propertiesMap = new HashMap<String, Map<String, String>>();
		Map<String, String> allProperties= new HashMap<String, String>();
		StringBuilder propLogMsg = new StringBuilder();
		try {
			propertiesMap = vc.retrievePropertiesMap();
			if (propertiesMap.isEmpty())
				throw new RuntimeException("No properties found");
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			for (Map.Entry<String, Map<String, String>> properties : propertiesMap.entrySet()) {
				String category = properties.getKey();
				for (Map.Entry<String, String> aProperty : properties.getValue().entrySet()) {
					String key = aProperty.getKey();
					String value = aProperty.getValue();
					allProperties.put(key,value);
					String propertyLine = String.format("%n%s=%s", key, value);
					if (category == "secrets")
						propLogMsg = propLogMsg.append(String.format("%n%s=%s", key, "**********"));
					else
						propLogMsg = propLogMsg.append(propertyLine);
					try {
						output.write(propertyLine.getBytes());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			resourceData = output.toByteArray();
			logger.info("The properties retreived are:" + propLogMsg.toString());
		} catch (Exception e) {
			throw new Exception("Property retrival failed");
		}

		//return new ByteArrayResource(resourceData);
		return allProperties;
	}

}
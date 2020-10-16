/*
 * (c) 2003-2018 MuleSoft, Inc. This software is protected under international copyright
 * law. All use of this software is subject to MuleSoft's Master Subscription Agreement
 * (or other master license agreement) separately entered into in writing between you and
 * MuleSoft. If such an agreement is not in place, you may not use the software.
 */
package com.xyzcompany.mulesoft.mule4;

import static org.mule.runtime.api.component.ComponentIdentifier.builder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mule.runtime.api.component.ComponentIdentifier;
import org.mule.runtime.config.api.dsl.model.ConfigurationParameters;
import org.mule.runtime.config.api.dsl.model.ResourceProvider;
import org.mule.runtime.config.api.dsl.model.properties.ConfigurationPropertiesProvider;
import org.mule.runtime.config.api.dsl.model.properties.ConfigurationPropertiesProviderFactory;
import org.mule.runtime.config.api.dsl.model.properties.ConfigurationProperty;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Properties;



/**
 * Builds the provider for a custom-properties-provider:config element.
 *
 * @since 1.0
 */
public class CustomConfigurationPropertiesProviderFactory implements ConfigurationPropertiesProviderFactory {

  public static final String EXTENSION_NAMESPACE =
      CustomConfigurationPropertiesExtensionLoadingDelegate.EXTENSION_NAME.toLowerCase().replace(" ", "-");
  private static final ComponentIdentifier CUSTOM_PROPERTIES_PROVIDER =
      builder().namespace(EXTENSION_NAMESPACE).name(CustomConfigurationPropertiesExtensionLoadingDelegate.CONFIG_ELEMENT).build();
  // TODO change to meaningful prefix
  private final static String CUSTOM_PROPERTIES_PREFIX = "usg::";
  private static final String TEST_KEY = "testKey";
  private static  Map<String, String> properties =null ;

  @Override
  public ComponentIdentifier getSupportedComponentIdentifier() {
    return CUSTOM_PROPERTIES_PROVIDER;
  }

  @Override
  public ConfigurationPropertiesProvider createProvider(ConfigurationParameters parameters,
                                                        ResourceProvider externalResourceProvider) {

    // This is how you can access the configuration parameter of the <custom-properties-provider:config> element.
    String customParameterValue = parameters.getStringParameter("customParameter");
    /*
    String userNameParameterValue = parameters.getStringParameter("userNameParameter");
    String passwordParameterValue = parameters.getStringParameter("passwordParameter");
    
    System.out.println("+userNameParameterValue " +userNameParameterValue);
    */
    
    this.secretsUrl = customParameterValue;
    if (isStringNull(secretsUrl))
  		throw new IllegalArgumentException("Required properties not supplied: secretsMgr.base.url");

    System.out.println("+createProvider " +customParameterValue);
    


    return new ConfigurationPropertiesProvider() {
/*
      @Override
      public Optional<ConfigurationProperty> getConfigurationProperty(String configurationAttributeKey) {
        // TODO change implementation to discover properties values from your custom source
        if (configurationAttributeKey.startsWith(CUSTOM_PROPERTIES_PREFIX)) {
          String effectiveKey = configurationAttributeKey.substring(CUSTOM_PROPERTIES_PREFIX.length());
          if (effectiveKey.equals(TEST_KEY)) {
            return Optional.of(new ConfigurationProperty() {

              @Override
              public Object getSource() {
                return "custom provider source";
              }

              @Override
              public Object getRawValue() {
                return customParameterValue;
              }

              @Override
              public String getKey() {
                return TEST_KEY;
              }
            });
          }
        }
        return Optional.empty();
      }*/
    	
		@Override
		public Optional<ConfigurationProperty> getConfigurationProperty(String configurationAttributeKey) {
		    System.out.println("+getConfigurationProperty");

		  // TODO change implementation to discover properties values from your custom source
		  //Map<String,String> properties=null;
		  validateProperties();
		  if(properties==null){
		    try{
		      properties  = getResources();
		    }
		    catch(Exception ex){
		      ex.printStackTrace();
		
		    }
		  }
	
		  if (configurationAttributeKey.startsWith(CUSTOM_PROPERTIES_PREFIX)) {
		    String effectiveKey = configurationAttributeKey.substring(CUSTOM_PROPERTIES_PREFIX.length());
		    if (properties.containsKey(effectiveKey)) {
		      final String value=properties.get(effectiveKey);
		      return Optional.of(new ConfigurationProperty() {
		
		        @Override
		        public Object getSource() {
		          return "secrets manager API";
		        }
		
		        @Override
		        public Object getRawValue() {
		          return value;
		        }
		
		        @Override
		        public String getKey() {
		          return effectiveKey;
		        }
		      });
		    }
		  }
		    
      System.out.println("-getConfigurationProperty");

	  return Optional.empty();
	}

      @Override
      public String getDescription() {
        // TODO change to a meaningful name for error reporting.
        return "USG properties provider";
      }
    };
    
  }
  
  
  
  private static final Logger logger = LogManager.getLogger(CustomConfigurationPropertiesProviderFactory.class.getName());
  //USG specific Variables
  private String username;
  private String password;
  private String secretsUrl;
  private String nonSecretsUrl;

  private boolean isStringNull(String str) {
    if (str == null || str.trim().isEmpty())
      return true;
    else
      return false;
  }
  public void validateProperties(){
/*    String appName = System.getProperty("api.name");
    if (isStringNull(appName))
      throw new IllegalArgumentException("Required properties not supplied: api.name");
    String appLayer = System.getProperty("api.layer");
    if (isStringNull(appLayer))
      throw new IllegalArgumentException("Required properties not supplied: api.layer");
    String appVersion = System.getProperty("api.version");
    if (isStringNull(appVersion))
      throw new IllegalArgumentException("Required properties not supplied: api.version");
    String majorVersion;
    Matcher appVersionPatternMatcher = Pattern.compile("^v(\\d+)").matcher(appVersion);
    if (appVersionPatternMatcher.find())
      majorVersion = appVersionPatternMatcher.group(0);
    else
      throw new IllegalArgumentException(
              "api.version property format is incorrect. Required format is vX.Y where X and Y are integers");
    String secretsMgrUrl = System.getProperty("secretsMgr.base.url");
    if (isStringNull(secretsMgrUrl))
      throw new IllegalArgumentException("Required properties not supplied: secretsMgr.base.url");

    String appId = appName + "-" + appLayer + "-" + majorVersion;
    String contextPath = "lobs/" + lobName + "/projects/" + projectId + "/applications/" + appId;
    this.secretsUrl = secretsMgrUrl + "/secrets/" + contextPath;
    this.nonSecretsUrl = secretsMgrUrl + "/non-secrets/" + contextPath + "/?business-domain=" + businessDomain;
    this.username = System.getProperty("app.ad.username");
    this.password = System.getProperty("app.ad.password");*/
	/*  
	String appName = System.getProperty("api.name");
	if (isStringNull(appName))
		throw new IllegalArgumentException("Required properties not supplied: api.name"); 
	
	String appVersion = System.getProperty("api.version");
	if (isStringNull(appVersion))
		throw new IllegalArgumentException("Required properties not supplied: api.version");
	
	String envId = System.getProperty("api.env");
	if (isStringNull(envId))
		throw new IllegalArgumentException("Required properties not supplied: api.env");
	*/
	String secretsMgrUrl = System.getProperty("secretsMgr.base.url");
	//if (isStringNull(secretsMgrUrl))
	//	throw new IllegalArgumentException("Required properties not supplied: secretsMgr.base.url");

	//String appId = appName + "-v" + appVersion;
	//String contextPath = "environments/" + envId + "/applications/" + appId;
	
	//this.secretsUrl = secretsMgrUrl + "/secrets/" + contextPath;
	//this.secretsUrl = secretsMgrUrl;
	this.username = System.getProperty("app.ad.username");
	this.password = System.getProperty("app.ad.password");

    logger.info("All app properties are valid. Calling Secrets Manager API to retreive application properties.");
  }

  public Map<String, String> getResources() throws Exception {
	System.out.println("+getResources");

    ConfigPropertiesLoader vpl = new ConfigPropertiesLoader(this.username, this.password, this.secretsUrl,
            this.nonSecretsUrl);
    Map<String, String> properties = vpl.getProperties();
	System.out.println("-getResources");
    return properties;

  }

}

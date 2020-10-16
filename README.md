<<<<<<< HEAD
# properties-loader-vault-mule4

This is an example that can be used to connect to a secrets provider to get secrets provider ID.

USAGE:
Update pom.xml to reflect the correct group where this application will be installed in artifactry.

Compile and install this application in your maven repository.

In your client application which will use thie module, do the following

1) Add dependency to mule-prop-loader to your project's pom.xml
<dependencies>
...
<dependency>
<groupId>com.[yourco]</groupId>
<artifactId>mule4-prop-loader</artifactId>
<version>1.0.0</version>
<classifier>mule-plugin</classifier>
</dependency>
...
</dependencies>

2. In Add Custom Properties Provider configuration in Studio
  Create a global.xml file under src/main/mule if one does not already exists
  Go to Global Configuration Elements view
  Click Create, and search for USG Properties Provider Config, and click OK
  Enter the API URL exposed by the vault to get the secure id in custom parameter field and click OK
=======
# properties-loader-vault-mule4
>>>>>>> a1ff6e2cf097613c7601f980e08bfb29087908e5

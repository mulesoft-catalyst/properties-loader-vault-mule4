# properties-loader-vault-mule4

This is an example that can be used to connect to a secrets provider to get secrets provider ID.

USAGE:
Update pom.xml to reflect the correct group where this application will be installed in artifactry.

Compile and install this application in your maven repository.

In your client application which will use this module, do the following

1) Add dependency to mule-prop-loader to your project's pom.xml
```
<dependencies>
...
<dependency>
<groupId>com.[yourco]</groupId>
<artifactId>mule4-prop-loader</artifactId>
<version>1.0.6</version>
<classifier>mule-plugin</classifier>
</dependency>
...
</dependencies>
```

2. In Add Custom Properties Provider configuration in Studio
 - Create a global.xml file under src/main/mule if one does not already exists
 - Go to Global Configuration Elements view
 - Click Create, and search for USG Properties Provider Config, and click OK
 - Enter the following
    1) API URL exposed by the vault to get the secure id in custom parameter field
    2) The user name required to login to the api
    3) Password required to log into the API
    
  <img src="https://user-images.githubusercontent.com/62348820/102548956-03953100-4081-11eb-86ee-7d2ea2e07648.png" width="400" height="400"/>
    <img src="https://user-images.githubusercontent.com/62348820/102549296-7c948880-4081-11eb-8056-7f814f49cda6.png" width="400" height="400"/>


The module will return the properties values  and can be retrieved using ${usg::<<propertyKey>>}


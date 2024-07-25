@description('Prefix name for web app components, accepts numbers and letters only.')
@minLength(3)
param appDnsPrefix string = 'fortify-demo-app${uniqueString(resourceGroup().id)}'

@description('User name for MySQL admin login.')
@minLength(3)
param mySqlAdminLogin string

@description('Password for MySQL admin login.')
@minLength(6)
@secure()
param mySqlAdminPassword string

@description('Location for all resources.')
param location string = resourceGroup().location

var storageAccountName = replace('${toLower(appDnsPrefix)}stg', '-', '')
var servicePlanName = '${appDnsPrefix}ServicePlan'
var webAppName = '${appDnsPrefix}-web'
var appInsightsName = '${appDnsPrefix}AppInsights'
var mySqlServerName = '${toLower(appDnsPrefix)}mysqlserver'
var mySqlDbName = '${toLower(appDnsPrefix)}mysqldb'

resource storageAccount 'Microsoft.Storage/storageAccounts@2017-06-01' = {
  sku: {
    name: 'Standard_RAGRS'
  }
  kind: 'Storage'
  name: storageAccountName
  location: location
  properties: {
    cors: {
      corsRules: [
        {
          allowedOrigins: [
            '*'
          ]
          allowedMethods: [
            'GET'
          ]
          maxAgeInSeconds: 0
          exposedHeaders: [
            '*'
          ]
          allowedHeaders: [
            '*'
          ]
        }
      ]
    }
  }
}

resource servicePlan 'Microsoft.Web/serverfarms@2016-09-01' = {
  sku: {
    name: 'B2'
  }
  kind: 'app'
  name: servicePlanName
  location: location
  properties: {
    name: servicePlanName
  }
}

resource webApp 'Microsoft.Web/sites@2016-08-01' = {
  kind: 'app'
  name: webAppName
  location: location
  properties: {
    serverFarmId: servicePlan.id
  }
}

resource webAppName_web 'Microsoft.Web/sites/config@2016-08-01' = {
  parent: webApp
  name: 'web'
  location: location
  properties: {
    javaVersion: '1.8'
    javaContainer: 'TOMCAT'
    javaContainerVersion: '9.0'
  }
}

resource webAppName_connectionstrings 'Microsoft.Web/sites/config@2016-08-01' = {
  parent: webApp
  name: 'connectionstrings'
  properties: {
    defaultConnection: {
      value: 'Database=${mySqlDbName};Data Source=${mySqlServer.properties.fullyQualifiedDomainName};User Id=${mySqlAdminLogin}@${mySqlServerName};Password=${mySqlAdminPassword}'
      type: 'MySql'
    }
  }
}

resource appInsights 'microsoft.insights/components@2015-05-01' = {
  kind: 'java'
  name: appInsightsName
  location: location
  tags: {
    'hidden-link:${resourceGroup().id}/providers/Microsoft.Web/sites/${webAppName}': 'Resource'
  }
  properties: {
    ApplicationId: appInsightsName
    Application_Type: 'web'
  }
}

resource mySqlServer 'Microsoft.DBforMySQL/servers@2017-12-01' = {
  location: location
  name: mySqlServerName
  properties: {
    createMode: 'Default'
    version: '5.7'
    storageMB: 51200
    administratorLogin: mySqlAdminLogin
    administratorLoginPassword: mySqlAdminPassword
    sslEnforcement: 'Disabled'
    backupRetentionDays: '7'
    geoRedundantBackup: 'Disabled'
  }
  sku: {
    name: 'B_Gen5_2'
    tier: 'Basic'
    capacity: 2
  }
}

resource mySqlServerName_mySqlServerName_Firewall 'Microsoft.DBforMySQL/servers/firewallrules@2017-12-01' = {
  parent: mySqlServer
  location: location
  name: '${mySqlServerName}Firewall'
  properties: {
    startIpAddress: '0.0.0.0'
    endIpAddress: '255.255.255.255'
  }
}

resource mySqlServerName_mySqlDb 'Microsoft.DBforMySQL/servers/databases@2017-12-01' = {
  parent: mySqlServer
  name: '${mySqlDbName}'
  properties: {
    charset: 'utf8'
    collation: 'utf8_general_ci'
  }
}

output webAppURL string = 'http://${webApp.properties.defaultHostName}'
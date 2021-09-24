#
# Example script to perform Fortify SCA static analysis
#

# Import some supporting functions
Import-Module $PSScriptRoot\modules\FortifyFunctions.psm1

# Import local environment specific settings
$EnvSettings = $(ConvertFrom-StringData -StringData (Get-Content ".\.env" | Where-Object {-not ($_.StartsWith('#'))} | Out-String))
$AppName = $EnvSettings['SSC_APP_NAME']
$AppVersion = $EnvSettings['SSC_APP_VER_NAME']
$SSCUrl = $EnvSettings['SSC_URL']
$SSCAuthToken = $EnvSettings['SSC_AUTH_TOKEN'] # CIToken
$ScanCentralCtrlUrl = $EnvSettings['SCANCENTRAL_CTRL_URL']
$ScanCentralCtrlToken = $EnvSettings['SCANCENTRAL_CTRL_TOKEN'] # ScanCentralCtrlToken
$ScanCentralPoolId = $EnvSettings['SCANCENTRAL_POOL_ID']
$ScanCentralEmail = $EnvSettings['SCANCENTRAL_EMAIL']
$ScanSwitches = "-Dcom.fortify.sca.Phase0HigherOrder.Languages=javascript,typescript -Dcom.fortify.sca.EnableDOMModeling=true -Dcom.fortify.sca.follow.imports=true -Dcom.fortify.sca.exclude.unimported.node.modules=true"

# Test we have Fortify installed successfully
Test-Environment
if ([string]::IsNullOrEmpty($ScanCentralCtrlUrl)) { throw "ScanCentral Controller URL has not been set" }
if ([string]::IsNullOrEmpty($ScanCentralCtrlToken)) { throw "ScanCentral Controller Token has not been set" }
if ([string]::IsNullOrEmpty($ScanCentralEmail)) { throw "ScanCentral Email has not been set" }
if ([string]::IsNullOrEmpty($SSCAuthToken)) { throw "SSC Authentication token has not been set" }
if ([string]::IsNullOrEmpty($AppName)) { throw "Application Name has not been set" }
if ([string]::IsNullOrEmpty($AppVersion)) { throw "Application Version has not been set" }

# Upload and run the scan

Write-Host Invoking ScanCentral...
Write-Host "scancentral -url $ScanCentralCtrlUrl -ssctoken $SSCAuthToken start -upload -uptoken $ScanCentralCtrlToken -b $AppName --application $AppName --application-version $AppVersion -email $ScanCentralEmail -bt mvn -bf pom.xml -scan"
& scancentral -url $ScanCentralCtrlUrl -ssctoken $SSCAuthToken start -bt mvn -bf pom.xml -upload `
    -uptoken $ScanCentralCtrlToken -b $AppName `
    --application $AppName --application-version $AppVersion  `
    -email $ScanCentralEmail

Write-Host
Write-Host You can check ongoing status with:
Write-Host " scancentral -url $ScanCentralCtrlUrl status -token [received-token]"

Write-Host Done.

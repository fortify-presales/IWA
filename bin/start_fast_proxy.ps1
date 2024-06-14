#
# Example script to start ScanCentral DAST FAST proxy
#

# Import some supporting functions
Import-Module $PSScriptRoot\modules\FortifyFunctions.psm1

# Import local environment specific settings
$EnvSettings = $(ConvertFrom-StringData -StringData (Get-Content ".\.env" | Where-Object {-not ($_.StartsWith('#'))} | Out-String))
$FastExe = $EnvSettings['FAST_EXE']
$FastPort = $EnvSettings['FAST_PORT']
$SCDastAPIUrl = $EnvSettings['SCANCENTRAL_DAST_API']
$SSCAuthToken = $EnvSettings['SSC_AUTH_TOKEN_BASE64'] # CIToken (Base64 encoded)
$SCDastCICDIdentifier = $EnvSettings['SCANCENTRAL_DAST_CICD_IDENTIFIER']

# Test we have Fortify installed successfully
Test-Environment
if ([string]::IsNullOrEmpty($FastExe)) { throw "FAST executable path has not been set" }
if ([string]::IsNullOrEmpty($FastPort)) { throw "FAST port has not been set" }
if ([string]::IsNullOrEmpty($SCDastAPIUrl)) { throw "ScanCentral DAST API has not been set" }
if ([string]::IsNullOrEmpty($SSCAuthToken)) { throw "SSC Authentication Token has not been set" }
if ([string]::IsNullOrEmpty($SCDastCICDIdentifier)) { throw "ScanCentral DAST CICD Identifier has not been set" }

Write-Host Starting FAST Proxy...
Write-Host "$FastExe -CIToken $SSCAuthToken -CICDToken $SCDastCICDIdentifier -u $SCDastAPIUrl -p $FastPort -n '"'FAST-Demo'"'"
Write-Host ""
& "$FastExe" -CIToken $SSCAuthToken -CICDToken $SCDastCICDIdentifier -u $SCDastAPIUrl -p $FastPort -n "FAST-Demo"
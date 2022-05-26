#
# Example script to start WebInspect FAST proxy
#

# Import some supporting functions
Import-Module $PSScriptRoot\modules\FortifyFunctions.psm1

# Import local environment specific settings
$EnvSettings = $(ConvertFrom-StringData -StringData (Get-Content ".\.env" | Where-Object {-not ($_.StartsWith('#'))} | Out-String))
$FastExe = $EnvSettings['FAST_EXE']
$FastPort = $EnvSettings['FAST_PORT']
$SSCAuthToken = $EnvSettings['SSC_AUTH_TOKEN_BASE64'] # CIToken (Base64 encoded)
$SCDastCICDIdentifier = $EnvSettings['SCANCENTRAL_DAST_CICD_IDENTIFIER']
$SCDastAPIUrl = $EnvSettings['SCANCENTRAL_DAST_API']

# Test we have Fortify installed successfully
Test-Environment
if ([string]::IsNullOrEmpty($FastExe)) { throw "FAST executable path has not been set" }
if ([string]::IsNullOrEmpty($FastPort)) { throw "FAST port has not been set" }

Write-Host Starting FAST Proxy...
& "$FastExe" -CIToken $SSCAuthToken -CICDToken $SCDastCICDIdentifier -u $SCDastAPIUrl -p $FastPort -k -n "FAST-Demo"
Write-Host Stopped FAST Proxy...
#
# Example script to stop ScanCentral DAST proxy
#

# Import some supporting functions
Import-Module $PSScriptRoot\modules\FortifyFunctions.psm1

# Import local environment specific settings
$EnvSettings = $(ConvertFrom-StringData -StringData (Get-Content ".\.env" | Where-Object {-not ($_.StartsWith('#'))} | Out-String))
$FastExe = $EnvSettings['FAST_EXE']
$FastPort = $EnvSettings['FAST_PORT']

# Test we have Fortify installed successfully
Test-Environment
if ([string]::IsNullOrEmpty($FastExe)) { throw "FAST executable path has not been set" }
if ([string]::IsNullOrEmpty($FastPort)) { throw "FAST port has not been set" }

Write-Host Stopping FAST Proxy...
Write-Host "$FastExe -p $FastPort -s"
Write-Host ""
& "$FastExe" -p $FastPort -s 
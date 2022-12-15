#
# Example script to perform Fortify ScanCentral SAST scan
#

# Parameters
param (
    [Parameter(Mandatory=$false)]
    [switch]$QuickScan
)

# Import some supporting functions
Import-Module $PSScriptRoot\modules\FortifyFunctions.psm1

# Import local environment specific settings
$EnvSettings = $(ConvertFrom-StringData -StringData (Get-Content ".\.env" | Where-Object {-not ($_.StartsWith('#'))} | Out-String))
$AppName = $EnvSettings['SSC_APP_NAME']
$AppVersion = $EnvSettings['SSC_APP_VER_NAME']
$SSCAuthToken = $EnvSettings['SSC_AUTH_TOKEN'] # CIToken
$ScanCentralCtrlUrl = $EnvSettings['SCANCENTRAL_CTRL_URL']
$ScanCentralPoolId = $EnvSettings['SCANCENTRAL_POOL_ID'] # Not yet used
$ScanCentralEmail = $EnvSettings['SCANCENTRAL_EMAIL']
$ScanSwitches = "-Dcom.fortify.sca.Phase0HigherOrder.Languages=javascript,typescript -Dcom.fortify.sca.EnableDOMModeling=true -Dcom.fortify.sca.follow.imports=true -Dcom.fortify.sca.exclude.unimported.node.modules=true"
$ScanArgs = @()
if ($QuickScan) {
    $ScanArgs += "-sargs"
    $ScanArgs += "`"-scan-precision 1`""
}

# Test we have Fortify installed successfully
Test-Environment
if ([string]::IsNullOrEmpty($ScanCentralCtrlUrl)) { throw "ScanCentral Controller URL has not been set" }
if ([string]::IsNullOrEmpty($ScanCentralEmail)) { throw "ScanCentral Email has not been set" }
if ([string]::IsNullOrEmpty($SSCAuthToken)) { throw "SSC Authentication token has not been set" }
if ([string]::IsNullOrEmpty($AppName)) { throw "Application Name has not been set" }
if ([string]::IsNullOrEmpty($AppVersion)) { throw "Application Version has not been set" }

# Package, upload and run the scan and import results into SSC
Write-Host Invoking ScanCentral SAST ...
Write-Host "scancentral -url $ScanCentralCtrlUrl start -upload -uptoken $SSCAuthToken -sp Package.zip -b $AppName -application $AppName -version $AppVersion -bt gradle -bf build.gradle -rules .\etc\sca-custom-rules.xml -filter .\etc\sca-filter.txt -email $ScanCentralEmail -block -o -f $($AppName).fpr $($ScanArgs)"
& scancentral -url $ScanCentralCtrlUrl start -upload -uptoken $SSCAuthToken -sp Package.zip `
    -b $AppName -application $AppName -version $AppVersion -bt gradle -bf build.gradle `
    -rules .\etc\sca-custom-rules.xml -filter .\etc\sca-filter.txt `
    -email $ScanCentralEmail -block -o -f "$($AppName).fpr" `
    $($ScanArgs)

# summarise issue count by analyzer
if ($SCALocalInstall -eq $True) {
    & fprutility -information -analyzerIssueCounts -project "$($AppName).fpr"
    Write-Host Generating PDF report...
    & ReportGenerator '-Dcom.fortify.sca.ProjectRoot=.fortify' -user "Demo User" -format pdf -f "$($AppName).pdf" -source "$($AppName).fpr"
}

#Write-Host
#Write-Host You can check ongoing status with:
#Write-Host " scancentral -url $ScanCentralCtrlUrl status -token [received-token]"

#
# Example script to perform Fortify Source and Lib Scanning with Sonatype
#

# Import some supporting functions
Import-Module $PSScriptRoot\modules\FortifyFunctions.psm1

# Import local environment specific settings
$EnvSettings = $(ConvertFrom-StringData -StringData (Get-Content ".\.env" | Where-Object {-not ($_.StartsWith('#'))} | Out-String))
$AppName = $EnvSettings['SSC_APP_NAME']
$AppVersion = $EnvSettings['SSC_APP_VER_NAME']
$SSCUrl = $EnvSettings['SSC_URL']
$SSCAuthToken = $EnvSettings['SSC_AUTH_TOKEN'] # CIToken
$NexusIQUrl = $EnvSettings['NEXUS_IQ_URL']
$NexusIQAuth = $EnvSettings['NEXUS_IQ_AUTH']
$NexusIQAppId = $EnvSettings['NEXUS_IQ_APP_ID']
$ScanSwitches = "-Dcom.fortify.sca.Phase0HigherOrder.Languages=javascript,typescript -Dcom.fortify.sca.EnableDOMModeling=true -Dcom.fortify.sca.follow.imports=true -Dcom.fortify.sca.exclude.unimported.node.modules=true"

# Test we have Fortify installed successfully
Test-Environment
if ([string]::IsNullOrEmpty($SSCUrl)) { throw "SSC URL has not been set" }
if ([string]::IsNullOrEmpty($SSCAuthToken)) { throw "SSC Authentication token has not been set" }
if ([string]::IsNullOrEmpty($NexusIQUrl)) { throw "Nexus IQ URL has not been set" }
if ([string]::IsNullOrEmpty($NexusIQAuth)) { throw "Nexus IQ Authentication has not been set" }
if ([string]::IsNullOrEmpty($NexusIQAppId)) { throw "Nexus IQ Application Id has not been set" }
if ([string]::IsNullOrEmpty($AppName)) { throw "Application Name has not been set" }
if ([string]::IsNullOrEmpty($AppVersion)) { throw "Application Version has not been set" }

# Check Source And Lib Analyzer is installed
if ((Get-Command "sourceandlibscanner.bat" -ErrorAction SilentlyContinue) -eq $null)
{
    Write-Error "sourceandlibscanner.bat is not in your PATH"
    Break
}

# Scan the application
Write-Host Scanning open source components ...
& sourceandlibscanner -auto -bt mvn -bf pom.xml  `
    -bc 'dependency:unpack-dependencies -DskipTests package' `
    -sonatype -iqurl $NexusIQUrl -nexusauth $NexusIQAuth -iqappid $NexusIQAppId -stage build -r iqReport.json `
    -upload -ssc $SSCUrl -ssctoken $SSCAuthToken -application $AppName -version $AppVersion
# -failonpolicywarnings
# -scan -f IWA.fpr

Write-Host Done.

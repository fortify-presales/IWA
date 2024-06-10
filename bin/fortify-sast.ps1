#
# Example script to perform Fortify Static Code Analysis
#

# Parameters
param (
    [Parameter(Mandatory=$false)]
    [ValidateSet('classic','security','devops')]
    [string]$ScanPolicy = "classic",
    [Parameter(Mandatory=$false)]
    [switch]$SkipPDF,
    [Parameter(Mandatory=$false)]
    [switch]$SkipSSC
)

# Import some supporting functions
Import-Module $PSScriptRoot\modules\FortifyFunctions.psm1

# Import local environment specific settings
$EnvSettings = $(ConvertFrom-StringData -StringData (Get-Content ".\.env" | Where-Object {-not ($_.StartsWith('#'))} | Out-String))
$AppName = $EnvSettings['SSC_APP_NAME']
$AppVersion = $EnvSettings['SSC_APP_VER_NAME']
$SSCUrl = $EnvSettings['SSC_URL']
$SSCAuthToken = $EnvSettings['SSC_AUTH_TOKEN'] # CIToken
$JVMArgs = "-Xss256M"
#$ScanSwitches = "-Dcom.fortify.sca.rules.enable_wi_correlation=true"
$ScanSwitches = "-Dcom.fortify.sca.rules.enable_wi_correlation=true -Dcom.fortify.sca.Phase0HigherOrder.Languages=javascript,typescript -Dcom.fortify.sca.EnableDOMModeling=true -Dcom.fortify.sca.follow.imports=true -Dcom.fortify.sca.exclude.unimported.node.modules=true"

# Test we have Fortify installed successfully
Test-Environment
if ([string]::IsNullOrEmpty($AppName)) { throw "Application Name has not been set" }

# Run the translation and scan

# Compile the application if not already built
$DependenciesFile = Join-Path -Path (Get-Location) -ChildPath "build\classpath.txt"
if (-not (Test-Path -PathType Leaf -Path $DependenciesFile)) {
    Write-Host Cleaning up workspace...
    & sourceanalyzer '-Dcom.fortify.sca.ProjectRoot=.fortify' -b "$AppName" -clean
    Write-Host Building application...
    & .\gradlew clean build writeClasspath -x test
}
$ClassPath = Get-Content -Path $DependenciesFile

Write-Host Running translation...
& sourceanalyzer '-Dcom.fortify.sca.ProjectRoot=.fortify' $JVMArgs $ScanSwitches -b "$AppName" `
    -jdk 11 -java-build-dir "build/classes" -cp $ClassPath -debug -verbose `
    -exclude "./src/main/resources/static/js/lib" -exclude "./src/main/resources/static/css/lib" `
    -exclude "./node_modules" -exclude "src/main/resources/schema.sql" -exclude "src/main/resources/data.sql" `
    "src/iac/**/*" "src/main/java/**/*" "src/main/resources/**/*" "Dockerfile*"

Write-Host Running scan...
& sourceanalyzer '-Dcom.fortify.sca.ProjectRoot=.fortify' $JVMArgs $ScanSwitches -b "$AppName" `
    -cp $ClassPath  -java-build-dir "target/classes" -debug -verbose `
    -rules etc/sast-custom-rules/example-custom-rules.xml -filter etc/sast-filters/example-filter.txt `
    -scan-policy $ScanPolicy `
    -build-project "$AppName" -build-version "$AppVersion" -build-label "SNAPSHOT" `
    -scan -f "$($AppName).fpr"

# summarise issue count by analyzer
& fprutility -information -analyzerIssueCounts -project "$($AppName).fpr"

if (-not $SkipPDF) {
    Write-Host Generating PDF report...
    & ReportGenerator '-Dcom.fortify.sca.ProjectRoot=.fortify' -user "Demo User" -format pdf -f "$($AppName).pdf" -source "$($AppName).fpr"
}

if (-not $SkipSSC) {
    Write-Host Uploading results to SSC...
    & fortifyclient uploadFPR -file "$($AppName).fpr" -url $SSCUrl -authtoken $SSCAuthToken -application $AppName -applicationVersion $AppVersion
}

Write-Host Done.

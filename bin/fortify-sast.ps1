#
# Example script to perform Fortify Static Code Analysis
#

# Parameters
param (
    [Parameter(Mandatory=$false)]
    [switch]$QuickScan,
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
if ($QuickScan) {
    $PrecisionLevel = 1
} else {
    $PrecisionLevel = 3 # or 4 for full scan
}

# Test we have Fortify installed successfully
Test-Environment
if ([string]::IsNullOrEmpty($AppName)) { throw "Application Name has not been set" }

# Run the translation and scan

# Compile the application if not already built
$DependenciesFile = Join-Path -Path (Get-Location) -ChildPath target\cp.txt
if (-not (Test-Path -PathType Leaf -Path $DependenciesFile)) {
    Write-Host Cleaning up workspace...
    & sourceanalyzer '-Dcom.fortify.sca.ProjectRoot=.fortify' -b "$AppName" -clean
    Write-Host Re-compiling application in debug mode...
    & mvn -P release,jar -DskipTests '-Dmaven.compiler.debuglevel="lines,vars,source"' verify package
    # write dependencies to file we can use later in sourceanalyzer command
    & mvn dependency:build-classpath '-Dmdep.outputFile=.\target\cp.txt'
}
$ClassPath = Get-Content -Path $DependenciesFile

Write-Host Running translation...
& sourceanalyzer '-Dcom.fortify.sca.ProjectRoot=.fortify' $JVMArgs $ScanSwitches -b "$AppName" `
    -jdk 11 -java-build-dir "target/classes" -cp $ClassPath -debug -verbose `
    -exclude ".\src\main\resources\static\js\lib" -exclude ".\src\main\resources\static\css\lib" -exclude ".\node_modules" `
    "src/main/java/**/*" "src/main/resources/**/*" "Dockerfile*"

Write-Host Running scan...
& sourceanalyzer '-Dcom.fortify.sca.ProjectRoot=.fortify' $JVMArgs $ScanSwitches -b "$AppName" `
    -cp $ClassPath  -java-build-dir "target/classes" -debug -verbose -rules etc/sca-custom-rules.xml `
    -scan-precision $PrecisionLevel -build-project "$AppName" -build-version "$AppVersion" -build-label "SNAPSHOT" -scan -f "$($AppName).fpr"

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

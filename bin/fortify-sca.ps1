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
$ScanSwitches = "-Dcom.fortify.sca.Phase0HigherOrder.Languages=javascript,typescript -Dcom.fortify.sca.EnableDOMModeling=true -Dcom.fortify.sca.follow.imports=true -Dcom.fortify.sca.exclude.unimported.node.modules=true"

# Test we have Fortify installed successfully
Test-Environment
if ([string]::IsNullOrEmpty($AppName)) { throw "Application Name has not been set" }

# Run the translation and scan

Write-Host Cleaning up workspace...
& sourceanalyzer '-Dcom.fortify.sca.ProjectRoot=.fortify' -b "$AppName" -clean

# Compile the application
Write-Host Re-compiling application in debug mode...
& mvn -P release,jar -DskipTests '-Dmaven.compiler.debuglevel="lines,vars,source"' verify package
# write dependencies to file we can use later in sourceanalyzer command
& mvn dependency:build-classpath '-Dmdep.outputFile=.\target\cp.txt'
$ClassPath = Get-Content -Path .\target\cp.txt

Write-Host Running translation...
& sourceanalyzer '-Dcom.fortify.sca.ProjectRoot=.fortify' $ScanSwitches -b "$AppName" `
    -jdk 1.8 -java-build-dir "target/classes" -cp $ClassPath -verbose `
    -exclude ".\src\main\resources\static\js\lib" -exclude ".\src\main\resources\static\css\lib" -exclude ".\node_modules" `
    "src/main/java/**/*" "src/main/resources/**/*" "Dockerfile*"

Write-Host Running scan...
& sourceanalyzer '-Dcom.fortify.sca.ProjectRoot=.fortify' $ScanSwitches -b "$AppName" `
    -cp $ClassPath  -java-build-dir "target/classes" -verbose `
   -build-project "$AppName" -build-version "$AppVersion" -build-label "SNAPSHOT" -scan -f "$($AppName).fpr"

Write-Host Generating PDF report...
& ReportGenerator '-Dcom.fortify.sca.ProjectRoot=.fortify' -user "Demo User" -format pdf -f "$($AppName).pdf" -source "$($AppName).fpr"

if (![string]::IsNullOrEmpty($SSCUrl)) {
    Write-Host Uploading results to SSC...
    & fortifyclient uploadFPR -file "$($AppName).fpr" -url $SSCUrl -authtoken $SSCAuthToken -application $AppName -applicationVersion $AppVersion
}

Write-Host Done.

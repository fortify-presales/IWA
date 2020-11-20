#
# Example script to perform Fortify SCA static analysis
#

$UploadToSSC = $True
$FortifySSCUrl = "http://localhost:8080/ssc/"
$FortifySSCAppId = 10000
$FortifySSCAppVersionId = 10000

# Check Maven is on the path
if ((Get-Command "mvn.cmd" -ErrorAction SilentlyContinue) -eq $null)
{
    Write-Error "Unable to find mvn.cmd in your PATH"
    Break
}

# Check Source Analyzer is on the path
if ((Get-Command "sourceanalyzer.exe" -ErrorAction SilentlyContinue) -eq $null)
{
    Write-Error "Unable to find sourceanalyzer.exe in your PATH"
    Break
}

# Check Fortify Client is installed
if ((Get-Command "fortifyclient.bat" -ErrorAction SilentlyContinue) -eq $null)
{
    Write-Host "fortifyclient.bat is not in your PATH, will not upload to SSC"
}

# Clean Project and scan results from previous run
Write-Host ************************************************************
Write-Host Cleaning project and scan results from previous run...
Write-Host ************************************************************
& mvn clean
& sourceanalyzer -Dcom.fortify.sca.ProjectRoot=.fortify -b iwa -clean

# Compile the application
Write-Host ************************************************************
Write-Host Re-compiling application in debug mode...
Write-Host ************************************************************
& mvn -P release,jar -DskipTests '-Dmaven.compiler.debuglevel="lines,vars,source"' verify package
# write dependencies to file we can use later in sourceanalyzer command
& mvn dependency:build-classpath '-Dmdep.outputFile=.\target\cp.txt'
$ClassPath = Get-Content -Path .\target\cp.txt

# Translate source files
Write-Host ************************************************************
Write-Host Translating source files...
Write-Host ************************************************************
& sourceanalyzer '-Dcom.fortify.sca.ProjectRoot=.fortify' -b iwa -jdk 1.8 -java-build-dir "target/classes" `
    -cp $ClassPath "src/main/java/**/*" "src/main/resources/**/*"

# Scan the application
Write-Host ************************************************************
Write-Host Scanning source files...
Write-Host ************************************************************
& sourceanalyzer '-Dcom.fortify.sca.ProjectRoot=.fortify' -b iwa -findbugs -cp $ClassPath  -java-build-dir "target/classes" `
	-build-project "Insecure Web App" -build-version "v1.0" -build-label "SNAPSHOT" -scan -f target\iwa.fpr
# -filter etc\sca-filter.txt


# Generate a PDF Report
Write-Host ************************************************************
Write-Host Generating PDF report
Write-Host ************************************************************
& ReportGenerator '-Dcom.fortify.sca.ProjectRoot=.fortify' -user "Demo User" -format pdf -f target\iwa.pdf -source target\iwa.fpr

# Upload results to SSC
Write-Host ************************************************************
Write-Host Uploading results to SSC
Write-Host ************************************************************
if ($UploadToSSC) {
	& fortifyclient uploadFPR -file target\iwa.fpr -url $FortifySSCUrl -authtoken $Env:SSC_AUTH_TOKEN -applicationVersionID $FortifySSCAppVersionId
}	

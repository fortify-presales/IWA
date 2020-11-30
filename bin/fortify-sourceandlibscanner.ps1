#
# Example script to perform Fortify Source and Lib Scanning with Sonatype
#

$FortifySSCUrl = "http://localhost:8080/ssc/"
$FortifySSCAppId = 10000
$FortifySSCAppVersionId = 10000
$NexusIQUrl = "http://localhost:8070"


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

# Check Source And Lib Analyzer is installed
if ((Get-Command "sourceandlibscanner.bat" -ErrorAction SilentlyContinue) -eq $null)
{
    Write-Host "sourceandlibscanner.bat is not in your PATH"
	Break
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
Write-Host Scanning source files and libs ...
Write-Host ************************************************************
& sourceandlibscanner -auto -scan -bt mvn -bf pom.xml -sonatype -iqurl $NexusIQUrl -nexusauth $Env:NEXUS_IQ_AUTH -iqappid IWA -stage build -r iqReport.json `
	-upload -ssc $FortifySSCUrl -ssctoken $Env:SSC_AUTH_TOKEN -versionid $FortifySSCAppVersionId
#& sourceandlibscanner -auto -scan -bt mvn -sonatype -libscanurl https://ds.sonatype.com -nexusauth $Env:NEXUS_AUTH_TOKEN `
#	-upload -ssc $FortifySSCUrl -ssctoken $Env:SSC_AUTH_TOKEN -versionid $FortifySSCAppVersionId

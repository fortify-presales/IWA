#
# Example script to perform Fortify Source and Lib Scanning with Sonatype
#

# You will need to have the following environment variables set:
#   NEXUS_IQ_URL    -   URL of Nexus IQ Server
#   NEXUS_IQ_AUTH   -   Nexus IQ User Token in the form "x:y"
#   SSC_URL         -   Fortify SSC URL
#   SSC_AUTH_TOKEN  -   Fortify SSC Authentication Token
#   SSC_APP_VER_ID  -   Fortify SSC Application Version Id

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

# Scan the application
Write-Host ************************************************************
Write-Host Scanning source files and libs ...
Write-Host ************************************************************
& sourceandlibscanner -auto -scan -bt mvn -bf pom.xml  `
    -bc 'dependency:unpack-dependencies -Dcom.fortify.sca.ProjectRoot=.fortify -Dclassifier=sources -DexcludeTransitive -DskipTests package' `
    -sonatype -iqurl $Env:NEXUS_IQ_URL -nexusauth $Env:NEXUS_IQ_AUTH -iqappid SpringEightBall -stage build -r iqReport.json -f SpringEightBall.json `
	-upload -ssc $Env:SSC_URL -ssctoken $Env:SSC_AUTH_TOKEN -versionid $SSC_VERSION_ID

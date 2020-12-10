#
# Example script to perform static application security testing (SAST) using Fortify ScanCentral
#

# SSC URL from environment variable
$ssccurl = $Env:SSC_URL 

# SSC ScanCentralCtrlToken from environment variable
# Can be created using: fortifyclient token -gettoken ScanCentralCtrlToken -url $scurl -user [your-username] -password [your-password] 
$ssctoken = $Env:SSC_SCANCENTRAL_CTRL_TOKEN

# SSC Application version id to upload results to from environment variable
# Can be retrieved using: fortifyclient listApplicationVersions -url $scurl -user [your-username] -password [your-password] 
$sscappid = $Env:SSC_APPLICATION_ID

# SSC AnalysisUploadToken token from environment variable
$sscuptoken = $Env:SSC_ANALYSIS_UPLOAD_TOKEN

# ScanCentral Pool UUID to use - Default Pool
$ScanCentralPool = "00000000-0000-0000-0000-000000000002"

# example path for ScanCentral client - SCA path
$env:Path += ";C:\Tools\scancentral\bin"


# Check ScanCentral client is on the path
if ((Get-Command "scancentral.bat" -ErrorAction SilentlyContinue) -eq $null)
{
    Write-Error "Unable to find scancentral.bat in your PATH"
    Break
}

# Start the scan using ScanCentral and Maven build tool
Write-Host ************************************************************
Write-Host Invoking ScanCentral...
Write-Host ************************************************************
# "C:\Program Files\Fortify\Fortify_SCA_and_Apps_20.2.1\bin\scancentral.bat" -sscurl http://ssc.mfdemouk.com -ssctoken b7d50934-69ab-49e0-b15b-33f63de4d6fc
# start -bt mvn -bf pom.xml -email kevin.lee@microfocus.com -pool 00000000-0000-0000-0000-000000000002 -filter etc\sca-filter.txt
& scancentral -sscurl $ssccurl -ssctoken $ssctoken start -upload -versionid $sscappid -b iwa -uptoken $sscuptoken -bt mvn -bf pom.xml

Write-Host You can check ongoing status with:
Write-Host "scancentral -sscurl $ssccurl -ssctoken $ssctoken status -token [returned-token]"

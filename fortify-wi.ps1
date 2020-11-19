#
# Example script to perform Fortify WebInspect dynamic analysis
#

# Application URL when deployed
$AppUrl = "http://localhost:8888"

# WebInspect policy to use - 1008 = Crtical and High only
$WIPolicyId = 1008

# example path for WebInspect
$env:Path += ";C:\Micro Focus\Fortify WebInspect"
$env:Path

# Check Maven is on the path
if ((Get-Command "mvn.cmd" -ErrorAction SilentlyContinue) -eq $null)
{
    Write-Error "Unable to find mvn.cmd in your PATH"
    Break
}

# Check Maven is on the path
if ((Get-Command "WI.exe" -ErrorAction SilentlyContinue) -eq $null)
{
    Write-Error "Unable to find WI.exe in your PATH"
    Break
}

# Generate API Settings for REST API scan
#Write-Host ************************************************************
#Write-Host Generating settings for API scan...
#Write-Host ************************************************************
#& wiswag -i ".\etc\WISwagConfig.json" -ice -it Swagger -wiOutput ApiScanSettings.xml

# Start the application using Maven
Write-Host ************************************************************
Write-Host Starting the application...
Write-Host ************************************************************
& mvn '-P war,wlp' '-DskipTests' clean package liberty:create liberty:install-feature liberty:deploy liberty:start

# Execute dynamic scan
Write-Host ************************************************************
Write-Host Executing dynamic scan...
Write-Host ************************************************************
& wi -s ".\etc\WebScanSettings.xml" -macro ".\etc\Login.webmacro" -u $AppUrl `
    -ep ".\target\wi-iwa.fpr" -ps $WIPolicyId

# Stop the application using Maven
Write-Host ************************************************************
Write-Host Stopping the application...
Write-Host ************************************************************
& mvn -Pwlp liberty:stop

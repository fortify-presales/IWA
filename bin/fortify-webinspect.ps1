#
# Example script to perform Fortify WebInspect dynamic analysis
#

# Application URL when deployed
$AppUrl = "http://localhost:8080"

# WebInspect policy to use - 1008 = Crtical and High only
$WIPolicyId = 1008

# example path for WebInspect
$env:Path += ";C:\Program Files\Fortify\Fortify WebInspect\"

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

# Execute dynamic scan
Write-Host ************************************************************
Write-Host Executing dynamic scan...
Write-Host ************************************************************
& wi -s ".\etc\IWA-UI-Dev-Settings.xml" -macro ".\etc\IWA-UI-Dev-Login.webmacro" -u $AppUrl `
    -ep ".\target\wi-iwa.fpr" -ps $WIPolicyId

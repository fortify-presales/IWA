#
# Example script to perform Fortify On Demand static scan using Fortify Command Line (fcli)
#

# Parameters
param (
    [Parameter(Mandatory=$false)]
    [switch]$ReBuild
)

# Import some supporting functions
Import-Module $PSScriptRoot\modules\FortifyFunctions.psm1

# Import local environment specific settings
$EnvSettings = $(ConvertFrom-StringData -StringData (Get-Content ".\.env" | Where-Object {-not ($_.StartsWith('#'))} | Out-String))
$AppName = $EnvSettings['SSC_APP_NAME']
$AppVersion = $EnvSettings['SSC_APP_VER_NAME']
$FoDApiUrl = $EnvSettings['FOD_API_URL']
$FoDUser = $EnvSettings['FOD_USER']
$FoDPassword = $EnvSettings['FOD_PASSWORD']
$FoDTenant = $EnvSettings['FOD_TENANT']
$PackageName = "FoDPackage.zip"

# Test we have Fortify installed successfully
Test-Environment
if ([string]::IsNullOrEmpty($AppName)) { throw "Application Name has not been set" }
if ([string]::IsNullOrEmpty($AppVersion)) { throw "Application Version has not been set" }
if ([string]::IsNullOrEmpty($FoDApiUrl)) { throw "FoD API URL has not been set" }
if ([string]::IsNullOrEmpty($FoDUser)) { throw "FoD User has not been set" }
if ([string]::IsNullOrEmpty($FoDPassword)) { throw "FoD Password has not been set" }
if ([string]::IsNullOrEmpty($FoDTenant)) { throw "FoD Tenant has not been set" }

# Check Package exists
if (Test-Path $PackageName) {
    $PackageExists = $true
} else {
    $PackageExists = $false
}

if ($ReBuild -or (-not $PackageExists))
{
    # Delete Package if it already exists
    if ($PackageExists) {
        Remove-Item $PackageName -Verbose
    }

    # Use scancentral to create Package
    Write-Host "Invoking ScanCentral Package ..."
    Write-Host "> scancentral package -bt gradle -bf build.gradle -bc `"clean build -x test`" -oss -o $PackageName"
    & scancentral package -bt gradle -bf build.gradle -bc "clean build -x test" -oss -o $PackageName
}

# Check Package exists
if (-not ($PackageExists)) {
    throw "ScanCentral package $PackageName does not exist, try the '-Rebuild' option"
}

Write-Host "Uploading $PackageName to $FoDApiUrl ..."
& fcli fod session login --url $FoDApiUrl --user $FoDUser --password $FoDPassword --tenant $FoDTenant --session iwa-fcli
& fcli fod scan start-sast "$($AppName):$($AppVersion)" --notes "fcli scan" -f $PackageName --store curScan --session iwa-fcli
Start-Sleep -s 5
Write-Host "Waiting until the scan has finished ..."
& fcli fod scan wait-for ::curScan:: --session iwa-fcli
& fcli fod session logout --session iwa-fcli

Write-Host "Scan complete".

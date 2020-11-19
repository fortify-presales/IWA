#
# Example script to perform static application security testing (SAST) using Fortify ScanCentral
#

# ScanCentral URL 
$ScanCentralUrl = "http://localhost:9880/scancentral-ctrl"

# ScanCentral Pool UUID to use - Defaullt Pool
$ScanCentralPool = "00000000-0000-0000-0000-000000000002"

# example path for ScanCentral client - SCA path
$env:Path += ";C:\Micro Focus\Fortify SCA and Apps 20.1.2\bin"
$env:Path

# Check ScanCentral client is on the path
if ((Get-Command "scancentral.bat" -ErrorAction SilentlyContinue) -eq $null)
{
    Write-Error "Unable to find scancentral.bat in your PATH"
    Break
}

$command = { scancentral.bat -url $ScanCentralUrl start -b iwa -bt mvn -bf pom.xml }

# Start the scan using ScanCentral and Maven built tool
Write-Host ************************************************************
Write-Host Invoking ScanCentral...
Write-Host ************************************************************
& scancentral -url $ScanCentralUrl start -b iwa -bt mvn -bf pom.xml -pool $ScanCentralPool | Tee-Object -FilePath "scancentral.run"

# get token and parse
# scancentral status -token 4cfac65f-1873-44c4-b0e9-8ff6fd4adebc

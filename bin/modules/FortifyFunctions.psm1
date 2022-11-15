
function Test-Environment {
    Write-Host "Validating Fortify Installation..." -NoNewline

	# Check Source Analyzer is on the path
    if ((Get-Command "sourceanalyzer.exe" -ErrorAction SilentlyContinue) -eq $null)
    {
        Write-Host
        throw "Unable to find sourceanalyzer.exe in your PATH"
    }

    # Check Fortify Client is installed
    if ((Get-Command "fortifyclient.bat" -ErrorAction SilentlyContinue) -eq $null)
    {
        Write-Error "fortifyclient.bat is not in your PATH, will not upload to SSC"
        $UploadToSSC = $False
    }

    # Check ScanCentral client is on the path
    if ((Get-Command "scancentral.bat" -ErrorAction SilentlyContinue) -eq $null)
    {
        Write-Error "Unable to find scancentral.bat in your PATH"
        Break
    }

    Write-Host "OK."
}

function Test-ThirdPartyEnvironment {
    Write-Host "Validating Third Party Installation..." -NoNewline

    # Check Maven is on the path
    if ((Get-Command "mvn" -ErrorAction SilentlyContinue) -eq $null)
    {
        Write-Host
        throw "Unable to find mvn in your PATH"
    }

    # Check Sonatype is installed
    if ((Get-Command "nexus-iq-cli.exe" -ErrorAction SilentlyContinue) -eq $null)
    {
        Write-Host
        throw "Unable to find nexus-iq-cli.exe is not in your PATH"
    }

    Write-Host "OK."
}

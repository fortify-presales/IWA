

function Test-Environment {
    $SCALocalInstall = $True
    $ScanCentralClient = $True
    $Fcli = $True

    $WarningPreference = "Continue"

    Write-Host "Checking Fortify Installation..." -NoNewline

    # Check Source Analyzer is on the path
    if ((Get-Command "sourceanalyzer.exe" -ErrorAction SilentlyContinue) -eq $null)
    {
        Write-Warning "Unable to find sourceanalyzer.exe in your PATH - local analysis and scan not available"
        $SCALocalInstall = $False
    }

    # Check FPR Utility is on the path
    if ((Get-Command "FPRUtility.bat" -ErrorAction SilentlyContinue) -eq $null)
    {
        Write-Warning "Unable to find FPRUtility.bat in your PATH - issue summaries not available"
        $SCALocalInstall = $False
    }

    # Check Report Generator is on the path
    if ((Get-Command "ReportGenerator.bat" -ErrorAction SilentlyContinue) -eq $null)
    {
        Write-Warning "Unable to find ReportGenerator.bat in your PATH - report generation not available"
        $SCALocalInstall = $False
    }

    # Check Fortify Client is installed
    if ((Get-Command "fortifyclient.bat" -ErrorAction SilentlyContinue) -eq $null)
    {
        Write-Warning "fortifyclient.bat is not in your PATH - upload to SSC not available"
        $SCALocalInstall = $False
    }

    # Check ScanCentral Client is installed
    if ((Get-Command "scancentral.bat" -ErrorAction SilentlyContinue) -eq $null)
    {
        if ($SCALocalInstall -eq $False) {
            Write-Host
            throw "scancentral.bat is not in your PATH - cannot run local or remote scan, exiting ..."
        }
        $ScanCentralClient = $False
    }

    # Check ScanCentral Client is installed
    if ((Get-Command "fcli.exe" -ErrorAction SilentlyContinue) -eq $null)
    {
        Write-Warning "fcli.exe is not in your PATH - some scripts will not run"
        $Fcli = $False
    }

    Write-Host "Done."
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

    Write-Host "Done."
}

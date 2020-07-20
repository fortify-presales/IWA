#
# Example script to perform Fortify On Demand static analysis
#

[CmdletBinding()]
param (
    [Parameter(Mandatory)]
    [string]$ZipFile = '.\fod.zip',

    [Parameter(Mandatory)]
    [int]$ReleaseId,

    [Parameter()]
    [string]$FODApiUri = $env:FOD_API_URI,

    [Parameter()]
    [string]$FODApiUsername = $env:FOD_API_USERNAME,

    [Parameter()]
    [string]$FODApiPassword = $env:FOD_API_PASSWORD,

    [Parameter()]
    [string]$FODApiGrantType = $env:FOD_API_GRANT_TYPE,

    [Parameter()]
    [string]$FODApiScope = $env:FOD_API_SCOPE,

    [Parameter()]
    [string]$Notes = 'Uploaded from PowerShellForFOD',

    [Parameter()]
    [int]$PollingInterval = 30
)
begin {
    if (-not (Test-Path -Path $ZipFile)) {
        Write-Error "File $ZipFile does not exist!" -ErrorAction Stop
    }

    # Make sure PowerShellForFOD Module is installed
    Write-Host "Installing PowerShellForFOD module ..."
    Install-Module PowerShellForFOD -Scope CurrentUser -Force -Repository PSGallery
}
process {

    # Configure API
    Write-Verbose "Configuring FOD API ..."
    $PWord = ConvertTo-SecureString -String $FODApiPassword -AsPlainText -Force
    $Credential = New-Object -TypeName System.Management.Automation.PSCredential -ArgumentList $FODApiUsername, $PWord
    Set-FODConfig -ApiUri $FODApiUri -GrantType $FODApiGrantType -Scope $FODApiScope
    Get-FODToken -Credential $Credential

    try
    {
        $release = Get-FODRelease -Id $FODReleaseId
        if ($release.releaseName) {
            Write-Host "Found release:" $release.releaseName "of application: " $release.applicationName
        } else{
            Write-Error "Could not find release with id: " $FODReleaseId
        }
    } catch {
        Write-Error "Error finding release: $_" -ErrorAction Stop
    }

    # Start Scan
    $fodZipFile = Resolve-Path -Path $ZipFile
    Write-Host "Uploading" $ZipFile "for scanning ..."
    $fodStaticScan = Start-FODStaticScan -ReleaseId $FODReleaseId -ZipFile $fodZipFile.Path -EntitlementPreference SubscriptionOnly `
        -RemediationScanPreference NonRemediationScanOnly -InProgressScanPreference DoNotStartScan `
        -Notes $Notes -Raw
    $ScanId = $fodStaticScan.scanId

    Write-Host "Polling status of scan id: $ScanId"
    do {
        Start-Sleep -s $PollingInterval # sleep for 30 seconds
        $fodScan = Get-FODScanSummary -ScanId $ScanId
        $ScanStatus = $fodScan.analysisStatusType
        Write-Host "Scan $ScanId status: $ScanStatus"
    } until (
        -not ($ScanStatus -eq "Queued" -or
            $ScanStatus -eq "In_Progress" -or
            $ScanStatus -eq "Cancelled")
    )

}
end {
    if ($Raw) {
        $fodScan
    } else {
        Write-Host "Finished scan with scan id: $ScanId"
    }
}

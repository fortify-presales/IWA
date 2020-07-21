#
# Example script to perform Fortify On Demand static analysis including verification and polling
#

[CmdletBinding()]
param (
    [Parameter(Mandatory)]
    [string]$ZipFile = '.\fod.zip',

    [Parameter(Mandatory)]
    [int]$ReleaseId,

    [Parameter(Mandatory)]
    [string]$FODApiUri,

    [Parameter(Mandatory)]
    [string]$FODApiUsername,

    [Parameter(Mandatory)]
    [string]$FODApiPassword,

    [Parameter()]
    [string]$FODApiGrantType = 'ClientCredentials',

    [Parameter()]
    [string]$FODApiScope = 'api-tenant',

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
        $Release = Get-FODRelease -Id $ReleaseId -Raw
        $ReleaseName = $Release.releaseName
        $ApplicationName = $Release.applicationName
        if ($ReleaseName) {
            Write-Host "Found release '$ReleaseName' of application '$ApplicationName'"
        } else{
            Write-Error "Could not find release with id: $ReleaseId" -ErrorAction Stop
        }
    } catch {
        Write-Error "Error finding release: $_" -ErrorAction Stop
    }

    Break

    # Start Scan
    $ZipFilePath = Resolve-Path -Path $ZipFile
    Write-Host "Uploading" $ZipFile "for scanning ..."
    $StaticScan = Start-FODStaticScan -ReleaseId $ReleaseId -ZipFile $ZipFilePath.Path -EntitlementPreference SubscriptionOnly `
        -RemediationScanPreference NonRemediationScanOnly -InProgressScanPreference DoNotStartScan `
        -Notes $Notes -Raw
    $ScanId = $StaticScan.scanId

    Write-Host "Polling status of scan id: $ScanId"
    do {
        Start-Sleep -s $PollingInterval # sleep for 30 seconds
        $ScanSummary = Get-FODScanSummary -ScanId $ScanId
        $ScanStatus = $ScanSummary.analysisStatusType
        Write-Host "Scan $ScanId status: $ScanStatus"
    } until (
        -not ($ScanStatus -eq "Queued" -or
            $ScanStatus -eq "In_Progress" -or
            $ScanStatus -eq "Cancelled")
    )

}
end {
    if ($Raw) {
        $ScanSummary
    } else {
        Write-Host "Finished scan with scan id: $ScanId"
    }
}

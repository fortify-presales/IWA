#
# Example script to perform Fortify On Demand static analysis including verification and polling
#

    [CmdletBinding()]
param (
    [Parameter(Mandatory)]
    [string]$ZipFile = '.\foD.zip',

    [Parameter(Mandatory)]
    [string]$ApplicationName,

    [Parameter(Mandatory)]
    [string]$ReleaseName,

    [Parameter(Mandatory)]
    [string]$FoDApiUri,

    [Parameter(Mandatory)]
    [string]$FoDApiKey,

    [Parameter(Mandatory)]
    [string]$FoDApiSecret,

    [Parameter()]
    [string]$FoDApiGrantType = 'ClientCredentials',

    [Parameter()]
    [string]$FoDApiScope = 'api-tenant',

    [Parameter()]
    [string]$Notes = 'Uploaded from PowerShellForFOD',

    [Parameter()]
    [int]$PollingInterval = 30,

    [Parameter()]
    [switch]$Raw
)
begin {
    if (-not (Test-Path -Path $ZipFile)) {
        Write-Error "File $ZipFile does not exist!" -ErrorAction Stop
    }

    # Make sure PowerShellForFoD Module is installed
    Write-Host "Installing PowerShellForFoD module ..."
    Install-Module PowerShellForFoD -Scope CurrentUser -Force -Repository PSGallery
}
process {

    # Configure API
    Write-Verbose "Configuring FoD API ..."
    $PWord = ConvertTo-SecureString -String $FoDApiSecret -AsPlainText -Force
    $Credential = New-Object -TypeName System.Management.Automation.PSCredential -ArgumentList $FoDApiKey, $PWord
    Set-FoDConfig -ApiUri $FoDApiUri -GrantType $FoDApiGrantType -Scope $FoDApiScope
    Get-FoDToken -Credential $Credential

    try
    {
        $ReleaseId = Get-FoDReleaseId -ApplicationName $ApplicationName -ReleaseName $ReleaseName
        if ($ReleaseId) {
            Write-Host "Found release id: '$ReleaseId' in application '$ApplicationName'"
        } else{
            Write-Error "Could not find release id for release: '$ReleaseName'" -ErrorAction Stop
        }
    } catch {
        Write-Error "Error finding release id: $_" -ErrorAction Stop
    }

    # Start Scan
    $ZipFilePath = (Resolve-Path -Path $ZipFile).Path
    Write-Host "Uploading" $ZipFile "for scanning ..."
    $StaticScan = Start-FoDStaticScan -ReleaseId $ReleaseId -ZipFile $ZipFilePath -EntitlementPreference SubscriptionOnly `
        -RemediationScanPreference NonRemediationScanOnly -InProgressScanPreference DoNotStartScan `
        -Notes $Notes -Raw
    $ScanId = $StaticScan.scanId

    Write-Host "Upload complete - started scan id: '$ScanId'"
    Write-Host "Polling status of scan ..."
    do {
        Start-Sleep -s $PollingInterval # sleep for X seconds
        $ScanSummary = Get-FoDScanSummary -Id $ScanId
        $ScanStatus = $ScanSummary.analysisStatusType
        Write-Host "Scan id: '$ScanId' status: $ScanStatus"
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
        Write-Host "$ScanStatus scan id: $ScanId"
    }
}

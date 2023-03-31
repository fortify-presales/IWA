#
# Example script to create a new report in FoD and download it
#

# Import some supporting functions
Import-Module $PSScriptRoot\modules\FortifyFunctions.psm1
# Import local environment specific settings
$EnvSettings = $(ConvertFrom-StringData -StringData (Get-Content ".\.env" | Where-Object {-not ($_.StartsWith('#'))} | Out-String))
$AppName = $EnvSettings['SSC_APP_NAME']
$AppId = $EnvSettings['SSC_APP_NAME_ID']
$AppVersion = $EnvSettings['SSC_APP_VER_NAME']
$AppVersionId = $EnvSettings['SSC_APP_VER_ID']
$AppUrl = $EnvSettings['APP_URL']
$SSCUrl = $EnvSettings['SSC_URL']
$ApiUri = $SSCUrl + "/api/v1"
$SSCAuthToken = $EnvSettings['SSC_AUTH_TOKEN_BASE64'] # Encoded CIToken
$SSCUsername = $EnvSettings['SSC_USERNAME']
$SSCPassword = $EnvSettings['SSC_PASSWORD']
$PollingInterval = 10

$TempReportName = [System.IO.Path]::GetRandomFileName()
$ReportName = $AppName + "Summary"
$ReportNotes = "Created Automatically via API"
$ReportFormat = "PDF" # or "HTML"

# This is an example report definition for creating an Application Summary
$Body = @"
{
  "name": "$TempReportName",
  "note": "$ReportNotes",
  "format": "$ReportFormat",
  "inputReportParameters": [
    {
      "name": "Include OWASP Top Ten 2021",
      "identifier": "includeOWASP2021",
      "paramValue": true,
      "type": "BOOLEAN"
    },
    {
      "name": "Include PCI DSS 3.2.1",
      "identifier": "includePCI321",
      "paramValue": true,
      "type": "BOOLEAN"
    },
    {
      "name": "Include PCI SSF 1.0",
      "identifier": "includePCISSF10",
      "paramValue": true,
      "type": "BOOLEAN"
    },
    {
      "name": "Include CWE",
      "identifier": "includeCWE",
      "paramValue": true,
      "type": "BOOLEAN"
    },
    {
      "name": "Include WASC 2.00",
      "identifier": "includeWASC2",
      "paramValue": true,
      "type": "BOOLEAN"
    },
    {
      "name": "Include DISA STIG 5.1",
      "identifier": "includeSTIG51",
      "paramValue": true,
      "type": "BOOLEAN"
    },
    {
      "name": "Include Appendix A",
      "identifier": "includeAppendixA",
      "paramValue": true,
      "type": "BOOLEAN"
    },
    {
      "name": "Include Appendix B",
      "identifier": "includeAppendixB",
      "paramValue": true,
      "type": "BOOLEAN"
    },
    {
      "name": "Include Appendix C",
      "identifier": "includeAppendixC",
      "paramValue": true,
      "type": "BOOLEAN"
    },
    {
      "name": "Include Appendix D",
      "identifier": "includeAppendixD",
      "paramValue": true,
      "type": "BOOLEAN"
    },
    {
      "name": "Include Appendix E",
      "identifier": "includeAppendixE",
      "paramValue": true,
      "type": "BOOLEAN"
    },
    {
      "name": "Include Appendix F",
      "identifier": "includeAppendixF",
      "paramValue": true,
      "type": "BOOLEAN"
    },
    {
      "name": "Application Version",
      "identifier": "projectversionid",
      "paramValue": $AppId,
      "type": "SINGLE_PROJECT"
    }
  ],
  "reportDefinitionId": 1,
  "type": "PROJECT",
  "project": {
    "id": $AppId,
    "name": "$AppVersion",
    "version": {
      "id": $AppVersionId,
      "name": "$AppName"
    }
  }
}
"@


#
# Create the report
#

Write-Host "Creating Report"
$Headers = @{
    'Accept' = "application/json"
    'Content-Type' = "application/json"
    'Authorization' = "FortifyToken $SSCAuthToken"
}
$Params = @{
    Uri = "$ApiUri/reports"
    ErrorAction = 'Stop'
    Method = 'POST'
    Body = $Body
}
try  {
    $Response = Invoke-RestMethod -Headers $Headers @Params
    #Write-Host $Response
} catch {
    Write-Error -Exception $_.Exception -Message "SSC API call failed: $_"
}

#
# Retrieve the id of the report
#
Write-Host -NoNewline "Retrieving Report Id: "
Start-Sleep -s 5
$Params = @{
    Uri = "$ApiUri/reports?start=0&limit=200&q=$TempReportName&fulltextsearch=true"
    ErrorAction = 'Stop'
    Method = 'GET'
}
try {
    $Response = Invoke-RestMethod -Headers $Headers @Params
    #Write-Host $Response.data
    $ReportId = $Response.data.id
    #Write-Host $ReportId
} catch {
    Write-Error -Exception $_.Exception -Message "SSC API call failed: $_"
}
Write-Host "$ReportId"
#
# TODO: Wait until the report has been sucessfully created or otherwise
#
Write-Host "Polling status of report"
$Params = @{
    Uri = "$ApiUri/reports/${ReportId}?fields=status"
    ErrorAction = 'Stop'
    Method = 'GET'
}
do {
    try  {
        $Response = Invoke-RestMethod -Headers $Headers @Params
        #Write-Host $Response.data
        $ReportStatus = $Response.data.status
    } catch {
        Write-Error -Exception $_.Exception -Message "SSC API call failed: $_"
    }
    Write-Host "Report id: '$ReportId' current status: $ReportStatus ..."
    if ($ReportStatus -ne "PROCESS_COMPLETE") {
        Start-Sleep -s $PollingInterval # sleep for X seconds
    }
} while ($ReportStatus -eq "PROCESSING" -or $ReportStatus -eq "SCHED_PROCESSING")
if ($ReportStatus -eq "ERROR_PROCESSING") {
    throw "There was an error processing the report..."
}
#
# Create a download token
#

Write-Host "Creating Download Token"
$Body = @"
{
    "fileTokenType": "REPORT_FILE"
}
"@
$Params = @{
    Uri = "$ApiUri/fileTokens"
    ErrorAction = 'Stop'
    Method = 'POST'
    Body = $Body
}
try {
    $Response = Invoke-RestMethod -Headers $Headers @Params
    #Write-Host $Response.data
    $TransferToken = $Response.data.token
    #Write-Host $TransferToken
} catch {
    Write-Error -Exception $_.Exception -Message "SSC API call failed: $_"
}

#
# Download the report
#

Write-Host "Downloading Report"
$AuthPair = "$($SSCUsername):$($SSCPassword)"
$EncodedCreds = [System.Convert]::ToBase64String([System.Text.Encoding]::ASCII.GetBytes($AuthPair))
$BasicAuthValue = "Basic $EncodedCreds"
$DownloadHeaders = @{
    'Authorization' = $BasicAuthValue
    'Accept' = "application/json"
    'Content-Type' = "application/json"
}

Invoke-WebRequest -Uri "$SSCUrl/transfer/reportDownload.html?mat=$TransferToken&id=$ReportId" `
    -Headers $DownloadHeaders -OutFile "$($ReportName).$($ReportFormat.ToLower())"

#
# Delete the report from SSC
#

Write-Host "Deleting Report from SSC"
$Params = @{
    Uri = "$ApiUri/reports/${ReportId}"
    ErrorAction = 'Stop'
    Method = 'DELETE'
}
try {
    $Response = Invoke-RestMethod -Headers $Headers @Params
    #Write-Host $Response.data
} catch {
    Write-Error -Exception $_.Exception -Message "SSC API call failed: $_"
}

Write-Host "Done."

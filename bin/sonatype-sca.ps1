#
# Example script to perform Sonatype Software Composition Analysis
#

# Parameters


# Import some supporting functions
Import-Module $PSScriptRoot\modules\FortifyFunctions.psm1

# Import local environment specific settings
$EnvSettings = $(ConvertFrom-StringData -StringData (Get-Content ".\.env" | Where-Object {-not ($_.StartsWith('#'))} | Out-String))
$NexusIqAppId = $EnvSettings['NEXUS_IQ_APP_ID']
$NexusIqUrl = $EnvSettings['NEXUS_IQ_URL']
$NexusIqAuthToken = $EnvSettings['NEXUS_IQ_AUTH']

# Test we have Fortify and Sonatype installed successfully
Test-ThirdPartyEnvironment
if ([string]::IsNullOrEmpty($NexusIqAppId)) { throw "Nexus IQ App Id has not been set" }
if ([string]::IsNullOrEmpty($NexusIqUrl)) { throw "Nexus IQ Url has not been set" }
if ([string]::IsNullOrEmpty($NexusIqAuthToken)) { throw "Nexus IQ Authentication token has not been set" }

Write-Host Uploading application for Sonatype SCA scan ...
& nexus-iq-cli.exe -i $NexusIqAppId -s $NexusIqUrl -a $NexusIqAuthToken -t build -r cli.log .\target\iwa.jar

Write-Host Done.

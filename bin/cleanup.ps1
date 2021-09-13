
# Import some supporting functions
Import-Module $PSScriptRoot\modules\FortifyFunctions.psm1

# Import local environment specific settings
$EnvSettings = $(ConvertFrom-StringData -StringData (Get-Content ".\.env" | Out-String))
$AppName = $EnvSettings['SSC_APP_NAME']

Write-Host "Removing files..."
Remove-Item -Force -Recurse ".fortify" -ErrorAction SilentlyContinue
Remove-Item "$($AppName).fpr" -ErrorAction SilentlyContinue
Remove-Item "$($AppName).pdf" -ErrorAction SilentlyContinue
Write-Host "Done."

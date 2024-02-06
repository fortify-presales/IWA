$EnvSettings = $(ConvertFrom-StringData -StringData (Get-Content ".\.env" | Where-Object {-not ($_.StartsWith('#'))} | Out-String))
$AppName = $EnvSettings['FOD_APP_NAME']
$RelName = $EnvSettings['FOD_REL_NAME']

scancentral package -bt gradle -bc 'clean build -x test' -o package.zip

fcli fod session login
fcli fod sast-scan start --release "$($AppName):$($RelName)" --notes "Started from CLI" -f package.zip --store curScan
fcli fod sast-scan wait-for ::curScan::
fcli fod sast-scan get ::curScan:: -o expr="Fortify Security Rating: {starRating}"
fcli fod session logout

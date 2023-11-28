
$EnvSettings = $(ConvertFrom-StringData -StringData (Get-Content ".\.env" | Where-Object {-not ($_.StartsWith('#'))} | Out-String))
$AppName = $EnvSettings['SSC_APP_NAME']
$AppVersion = $EnvSettings['SSC_APP_VER_NAME']

fcli ssc session login
#fcli ssc issue list-filtersets --appversion "$($AppName):$($AppVersion)"
fcli ssc issue count --appversion "$($AppName):$($AppVersion)" --filterset "Security Gate"
fcli ssc session logout

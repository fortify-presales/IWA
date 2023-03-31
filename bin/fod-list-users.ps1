# Example FoD script to login and list users with paging

### Change the following to your details:
$ApiUri = "https://api.ams.fortify.com"
$Username = "lasalas"
$Password = "ENTER_YOUR_PASSWORD"
$Tenant = "tam_team_test"

$Scope = "api-tenant"

$VerbosePreference = "Continue"

### DO NOT CHANGE ANY OF THE FOLLOWING

### authenticate

$Body = @{
	grant_type = 'password'
	scope = $Scope
	username = $Tenant + "\" + $Username
	password = $Password
}

$Uri = "$ApiUri/oauth/token"

$Params = @{
	ErrorAction = 'Stop'
	Verbose = $true
}
	
#try {
#	$Response = $null
#	$Response = Invoke-RestMethod -Uri $Uri @Params -Method Post -Body $Body
#} catch {
#	if ($_.ErrorDetails.Message -ne $null) {
#		Write-Error $_.ErrorDetails
#	} else {
#		Write-Error -Exception $_.Exception -Message "FOD API call failed: $_"
#	}
#}

#if ($Response -ne $null -and $Response.ok -eq $False) {
#	$Response
#} elseif ($Response) {
#	$Token = $Response.access_token
#	Write-Verbose $Token
#} else {
#	Write-Verbose "Something went wrong.  `$Response is `$null"
#}	

$Token="gAAAAGqI8JjyfIK-5Q0PeoPw3miM3zNQUja2xF5o23jUIuzejifXgJZgSbBLuTGQdteJ2yKVJiCrvDkRI0fZxDEtDwn33-NeAMJhLO2Rw4lXGPXGnQkDtfUIb-PvFTiIv96ucdIx95j5H6H5l_ab3tRjiEOg6qgBQy_bTvytJU6Q7rM1lAMAAIAAAAAltDVAlVs_qwOw3pXSoUXbsGqnS-5O1PknAHuVay--6HrGoNfMS6ONeXnRrlzW3od5GtsE1KVf95cRuT8KLYR2oG_z7naQHQ4Ndl_wcsbHKj3ON4Nz1UyRu92cZlk5sbMrzXx-BUNEov8WpZg_gIZTM9-VDV7ntDElquzRKsEPBvTL2ebDG_F_juhKreeEmAHZ0BHxJIk_bWoI4-1HNxMsDxoZe_wLBzvSh30bziOYRADdOjJhG9QiY-Oindmoe_dqIeHnkuf1KQvj9sdINABCY69BkISJz1m7q71W-sdRrN70Bj8llPIiOPAToDVfenExymDFrwSV4kPGngdzsc_WRSNKmjcQMSWfIE9bYX5rvewQTOinUn4A6LDVDfIojlpeZPdARyn-5KsxYofo8adrqIKey2CO2nW5RUajMXkwyF6O8-kWbtVGFL5swPP_-pZPDjGTb7HNcVpuJSsEwT1Ph36RuNt3CDZxM5sc-ZOR3WmkhT2mx89EMhiJ17_MsHj0z6vfyrpAH8iA52XmJWu4JUGIkINdRZ_rH2ZRDwoHB6ilSJWg6oHhFb8Vti8EB_w792E03NFOT6g0ak0qWB_anx2rbDyICMapfH19IyIBc338jfCU06bp4URkgya8JKtvwRMUNI-Lkc8awIf0yrmF_OlCpEXyeavtVIr3hQy1id3JBZMcX0HPEFMVqxCQHHP-3ZyC1aHrdlHNTyNUuQ8Wzl4KQb6OHU32buBIgufrz59hiM1qDGwlRatGyj-R2MXs1OzcESFWMcDuMm0FvSAvpJh6HaY2SGp2qVtN00lmReIySDoRUQJwZjy4Rd7c7So-rTvfHTUCm-LzS33h5emiiS5Og2U4D7m2oMahZYObHYxWxXccv2FOJ-bWYDP65q5a_EsvPfsq2dB4Xc9JW1X5DCaIX-7nMDSNauHTiMYDUTipAfSJ30WII2Oznz9KcR1FyOw5JX4vdlS6GyILntX2SUWWs9SVWZiMuLixIb1ya8EWJr8ZqEAOlcF6b_ygQCrOrI5ucGSgU1xQ--t4xZ6cn7VMV8bCMUbFxj0Xckhd0tjjfgOpFkOdJTYK-TyXWfkLavoDUMjSozK4HGVwTNGSiAGHfcnCoNMHstKNnvavR1gxFk4KUgMEjxxhCs1Gr7CbQ8kEvPL4_x1XbHvypTbShCNeedoLgKka8PdHg-9Yyg16aid9hVVOlmAMkqyhXZg"
### list users

$Uri = "$ApiUri/api/v3/releases"

$Users = @()
$HasMore = $false
$TotalCount = 0
$LoadedCount = 0
$LoadLimit = 50

$Body = @{
	offset = 0
	limit = $LoadLimit
}

$Headers = @{
	'Authorization' = "Bearer " + $Token
	'Accept' = "application/json"
}

do {
	$Response = $null
	try {
		$Response = Invoke-RestMethod -Uri $Uri -Headers $Headers @Params -Method Get -Body $Body
	} catch {
		if ($_.ErrorDetails.Message -ne $null) {
			Write-Error $_.ErrorDetails
		} else {
			Write-Error -Exception $_.Exception -Message "FOD API call failed: $_"
		}
	}	
	$TotalCount = $Response.totalCount
	if ($TotalCount -lt $LoadLimit) {
		$LoadedCount += $TotalCount
		$HasMore = $false
	} elseif ($LoadedCount -lt ($TotalCount - $LoadLimit)) {
		$HasMore = $true
		$LoadedCount += $LoadLimit
		$Body.Remove("offset")
		$Body.Add("offset", $LoadedCount)
	} else {
		$LoadedCount += $TotalCount
		$HasMore = $false
	}
	$Users += $Response.items
} until (-not $HasMore)
Write-Verbose "Loaded $LoadedCount users"

$Users

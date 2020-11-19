def runWebInspectScan(wiApiUrl, settingsName, scanName, scanUrl, loginMacroName, policyId) {
	def scanId
	def scanStatus
	
	// start a new scan
	def postUrl = "${wiApiUrl}/scanner/scans"
	println "WebInspect POST request: $postUrl with body:"
	def post = new URL(postUrl).openConnection()
	def body = """\
	{
		"settingsName": "$settingsName",
		"overrides": {
			"scanName": "$scanName",
			"startUrls": [ "$scanUrl" ],
			"loginMacro": "$loginMacroName",
			"policyId": $policyId
		}
	}
	"""
	println body
	post.setRequestMethod("POST")
	post.setDoOutput(true)
	post.setRequestProperty("Accept", "application/json")
	post.setRequestProperty("Content-Type", "application/json")
	post.getOutputStream().write(body.getBytes("UTF-8"))
	def postRC = post.getResponseCode()
	if (postRC.equals(200) || postRC.equals(201)) {
		def parsedJson = new groovy.json.JsonSlurper().parseText(post.getInputStream().getText())
		scanId = parsedJson.ScanId
		println "Started scan Id: $scanId"
	} else {
		println "Got error response: $postRC"
		def parsedJson = new groovy.json.JsonSlurper().parseText(post.getInputStream().getText())
		if (parsedJson) { println parsedJson.Message }
		return false
	}
	
	do {
		println "Polling status of scan id: $scanId"
		sleep(5000)
		// get status of scan
		def getUrl = "${wiApiUrl}/scanner/scans/${scanId}?action=WaitForStatusChange"
		println "WebInspect GET request: $getUrl"
		def get = new URL(getUrl).openConnection()
		get.setRequestMethod("GET")
		get.setDoOutput(true)
		get.setRequestProperty("Accept", "application/json")
		def getRC = get.getResponseCode()
		if (getRC.equals(200) || getRC.equals(201)) {
			def parsedJson = new groovy.json.JsonSlurper().parseText(get.getInputStream().getText())
			scanStatus = parsedJson.ScanStatus
			println "Scan Status: $scanStatus"
		}
	} while (scanStatus == "NotRunning" || scanStatus == "Running")	

	// TODO: download scan results as FPR

}

// example invocation
//runWebInspectScan("http://localhost:8083/webinspect", "IWA-UI", "IWA Web Scan", "http://localhost:8888", "Login", 1008)
return this
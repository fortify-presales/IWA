class edastApi {
	String apiUri
	String authToken
	boolean debug

	edastApi() {
		this.debug = false
	}

	edastApi(String apiUri, String authToken) {
		this.apiUri = apiUri
		this.authToken = authToken
		this.debug = false
	}

	def setApiUri(String apiUri) {
		this.apiUri = apiUri
	}

	def setAuthToken(String authToken) {
		this.authToken = authToken
	}

	def setDebug(boolean debug) {
		this.debug = debug
	}

	def startScan(String scanName,  String cicdToken) {
		def scanId = 0

		// TODO: ensure edastApiUrl does not end in slash "/"

		// start a new scan
		def postUrl = "${this.apiUri}/scans/start-scan-cicd"
		if (debug) println "ScanCentral DAST POST request: $postUrl with body:"
		def post = new URL(postUrl).openConnection()
		def body = """\
		{
			"name": "$scanName",
			"cicdToken": "$cicdToken"
		}
		"""
		if (debug) println body
		post.setRequestMethod("POST")
		post.setDoOutput(true)
		post.setRequestProperty("Accept", "application/json")
		post.setRequestProperty("Content-Type", "application/json")
		post.setRequestProperty("Authorization", "FORTIFYTOKEN " + this.authToken)
		post.getOutputStream().write(body.getBytes("UTF-8"))
		def postRC = post.getResponseCode()
		if (postRC.equals(200) || postRC.equals(201)) {
			def parsedJson = new groovy.json.JsonSlurper().parseText(post.getInputStream().getText())
			scanId = parsedJson.id
		} else {
			println "Got error response: $postRC"
			def parsedJson = new groovy.json.JsonSlurper().parseText(post.getInputStream().getText())
			if (parsedJson) {
				println parsedJson.Message
			}
		}

		return scanId
	}

	def getScanStatusValue(Integer scanStatusId) {
		def scanStatusValue = ""

		// get status string value of scan
		def getUrl = "${this.apiUri}/utilities/lookup-items?type=ScanStatusTypes"
		if (debug) println "ScanCentral DAST GET request: $getUrl"
		def get = new URL(getUrl).openConnection()
		get.setRequestMethod("GET")
		get.setDoOutput(true)
		get.setRequestProperty("Accept", "application/json")
		get.setRequestProperty("Authorization", "FORTIFYTOKEN " + this.authToken)
		def getRC = get.getResponseCode()
		if (getRC.equals(200) || getRC.equals(201)) {
			def parsedJson = new groovy.json.JsonSlurper().parseText(get.getInputStream().getText())
			if (debug) println parsedJson
			parsedJson.each { v ->
				if (Integer.valueOf(v.value) == Integer.valueOf(scanStatusId)) {
					scanStatusValue = v.text
					return
				}
			}
		}

		return scanStatusValue

	}

	def getScanStatusId(Integer scanId) {
		def scanStatus = 0

		// get status of scan
		def getUrl = "${this.apiUri}/scans/${scanId}/scan-summary"
		if (debug) println "ScanCentral DAST GET request: $getUrl"
		def get = new URL(getUrl).openConnection()
		get.setRequestMethod("GET")
		get.setDoOutput(true)
		get.setRequestProperty("Accept", "application/json")
		get.setRequestProperty("Authorization", "FORTIFYTOKEN " + this.authToken)
		def getRC = get.getResponseCode()
		if (getRC.equals(200) || getRC.equals(201)) {
			def parsedJson = new groovy.json.JsonSlurper().parseText(get.getInputStream().getText())
			if (debug) println parsedJson
			scanStatus = parsedJson.item.scanStatusType
		}
		return scanStatus
	}

	def startScanAndWait(String scanName, String cicdToken, Integer sleepTime) {
		Integer scanId = this.startScan(scanName, cicdToken)
		if (debug) println "Started scan with id: ${scanId}"

		def isScanActive = true
		def scanInActiveRange1 = 5..7
		def scanInActiveRange2 = 15..17
		def scanStatus = ""
		while (isScanActive) {
			Integer scanStatusId = this.getScanStatusId(scanId)
			scanStatus = this.getScanStatusValue(scanStatusId)
			if (debug) println "Scan status ${scanStatusId} - ${scanStatus}"
			if (scanInActiveRange1.contains(scanStatusId) || scanInActiveRange2.contains(scanStatusId)) {
				isScanActive = false
			} else {
				sleep(sleepTime * 1000)
			}
		}

		if (debug) println "Scan id: ${scanId} - ${scanStatus}"
		return scanId
	}
}

// example invocation

/*
def edastApi = new edastApi("http://localhost:8500/api", "SSC_CITOKEN")
edastApi.startScanAndWait("Test Scan", "SCAN_SETTINGS_ID", 5)
*/

return new edastApi()
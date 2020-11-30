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

	def startScan(scanName, cicdToken) {
		def scanId

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
			return false
		}

		return scanId
	}

	def getScanStatusValue(scanStatusId) {
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

	def getScanStatus(scanId) {
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
}

// example invocation

/*
def edastApi = new edastApi("http://fortify.mfdemouk.com:8500/api", "MmEyYjkyNjMtYWQ5MC00MjFmLTg4ODItZjZjN2JhYjliZGI1")
def scanId = edastApi.startScan("Jenkins initiated scan", "31279b79-376a-46e7-90b1-2fbe11cfbb2e")
println "Started scan with id: ${scanId}"

def isScanActive = true
def scanInActiveRange1 = 5..7
def scanInActiveRange2 = 15..17
def scanStatus = ""
while (isScanActive) {
	def scanStatusId = edastApi.getScanStatus(scanId)
	if (scanInActiveRange1.contains(scanStatusId) || scanInActiveRange2.contains(scanStatusId)) {
		isScanActive = false
	} else {
		scanStatus = edastApi.getScanStatusValue(scanStatusId)
		println "Scan status: ${scanStatus} ..."
		sleep(5000)
	}
}
println "Scan id: ${scanId} - ${scanStatus}"
*/

return new edastApi()
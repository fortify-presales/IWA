#!/usr/bin/env groovy

//********************************************************************************
// An example pipeline for DevSecOps using Micro Focus Fortify Application Security products
// @author: Kevin A. Lee (kevin.lee@microfocus.com)
//
//
// Pre-requisites:
// - Fortify SCA/ScanCentral SAST has been installed (for on-premise SAST)
// - Fortify WebInspect/ScanCentral DAST has been installed (for on-premise DAST)
// - A Fortify On Demand account and API access are available (for cloud based SAST)
// - Docker Pipeline plugin has been installed (Jenkins)
// - [Optional] Sonatype Nexus IQ server has been installed for OSS vulnerabilities
//
// Typical node setup:
// - Install Fortify SCA on the agent machine
// - create a new Jenkins agent for this machine
// - Apply the label "fortify" to the agent.
// - Set the environment variable "FORTIFY_HOME" on the agent point to the location of the Fortify SCA installation
// Also ensure the label "master" has been applied to your Jenkins master.
//
// Credentials setup:
// Create the following "Secret text" credentials in Jenkins and enter values as follows:
//		iwa-git-creds-id		    - Git login as Jenkins "Username with Password" credential
//      iwa-ssc-ci-token-id         - Fortify Software Security Center "CIToken" authentication token as Jenkins Secret credential
//      iwa-edast-auth-id           - Fortify Scan Central DAST authentication as "Jenkins Username with Password" credential
//      iwa-fod-release-id          - Fortify on Demand Release Id as Jenkins Secret credential
//      iwa-nexus-iq-token-id       - Sonatype Nexus IQ user token in form of "user code:pass code"
//      iwa-dockerhub-creds-id      - DockerHub login as Jenkins "Username with Password" credential
// All of the credentials should be created (with empty values if necessary) even if you are not using the capabilities.
// For Fortify on Demand (FOD) Global Authentication is used rather than Personal Access Tokens.
//
//********************************************************************************

// The instances of Docker image and container that are created
def dockerImage
def dockerContainer
def dockerContainerName = "iwa-jenkins"

pipeline {
    agent any

    //
    // The following parameters can be selected when the pipeline is executed manually to execute
    // different capabilities in the pipeline or configure the servers that are used.

    // Note: the pipeline needs to be executed at least once for the parameters to be available
    //
    parameters {
        booleanParam(name: 'SCA_LOCAL',       	defaultValue: params.parameterName ?: false,
            description: 'Use (local) Fortify SCA for Static Application Security Testing')
        booleanParam(name: 'SCA_OSS',      defaultValue: params.parameterName ?: false,
            description: 'Use Fortify SCA with Sonatype Nexus IQ for Open Source Susceptibility Analysis')
        booleanParam(name: 'SCANCENTRAL_SAST', 	defaultValue: params.parameterName ?: false,
            description: 'Run a remote scan using Scan Central SAST (SCA) for Static Application Security Testing')
        booleanParam(name: 'SCANCENTRAL_DAST', 	defaultValue: params.parameterName ?: false,
            description: 'Run a remote scan using Scan Central DAST (WebInspect) for Dynamic Application Security Testing')
        booleanParam(name: 'UPLOAD_TO_SSC',		defaultValue: params.parameterName ?: false,
            description: 'Enable upload of scan results to Fortify Software Security Center')             
        booleanParam(name: 'FOD',       	    defaultValue: params.parameterName ?: false,
            description: 'Use Fortify on Demand for Static Application Security Testing')
        booleanParam(name: 'RELEASE_TO_DOCKERHUB', defaultValue: params.parameterName ?: false,
                description: 'Release built and tested image to Docker Hub')
        string(name: 'SSC_URL',                 defaultValue: params.parameterName ?: "http://ssc.mfdemouk.com",
                description: 'URL of SSC')
        string(name: 'SSC_APP_VERSION_ID',      defaultValue: params.parameterName ?: "10002",
                description: 'Id of Application in SSC to upload results to')
        string(name: 'SSC_NOTIFY_EMAIL',        defaultValue: params.parameterName ?: "do-not-reply@microfocus.com",
                description: 'User to notify with SSC/ScanCentral information')
        string(name: 'SSC_SENSOR_POOL_UUID',    defaultValue: params.parameterName ?: "00000000-0000-0000-0000-000000000002",
                description: 'UUID of Scan Central Sensor Pool to use - leave for Default Pool')
        string(name: 'EDAST_URL',               defaultValue: params.parameterName ?: "http://scancentral.mfdemouk.com/api",
                description: 'ScanCentral DAST API URI')
        string(name: 'EDAST_CICD',              defaultValue: params.parameterName ?: "37512d59-7863-48c2-8d77-1a57c9f1ec6a",
                description: 'ScanCentral DAST CICD identifier')
        string(name: 'NEXUS_IQ_URL',            defaultValue: params.parameterName ?: "https://sonatype.mfdemouk.com",
                description: 'Nexus IQ URL')
        string(name: 'DOCKER_ORG',              defaultValue: params.parameterName ?: "mfdemouk",
                description: 'Docker organisation (in Docker Hub) to push released images to')
    }

    environment {
        //
        // Application settings
        //
		APP_NAME = "IWA-Java"                      		// Application name
        APP_VER = "master"                                  // Application release - GitHub master branch
        COMPONENT_NAME = "iwa"                              // Component name
        GIT_URL = scm.getUserRemoteConfigs()[0].getUrl()    // Git Repo
        GIT_CREDS = credentials('iwa-git-creds-id')
        JAVA_VERSION = 8                                    // Java version to compile as
        ISSUE_IDS = ""                                      // List of issues found from commit
        FOD_RELEASE_ID = credentials('iwa-fod-release-id')
        FOD_UPLOAD_DIR = 'fod'                              // Directory where FOD upload Zip is constructed
        SSC_AUTH_TOKEN = credentials('iwa-ssc-ci-token-id')
        EDAST_AUTH = credentials('iwa-edast-auth-id')
        NEXUS_IQ_AUTH_TOKEN = credentials('iwa-nexus-iq-token-id')
	}

    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven 'M3'
    }

    stages {
        stage('Build') {
            // Run on "master" node
            agent { label 'master' }
            steps {
                // Get some code from a GitHub repository
                git credentialsId: 'iwa-git-creds-id', url: "${env.GIT_URL}"

                // Get Git commit details
                script {
                    if (isUnix()) {
                        sh 'git rev-parse HEAD > .git/commit-id'
                    } else {
                        bat(/git rev-parse HEAD > .git\\commit-id/)
                    }
                    //bat(/git log --format="%ae" | head -1 > .git\commit-author/)
                    env.GIT_COMMIT_ID = readFile('.git/commit-id').trim()
                    //env.GIT_COMMIT_AUTHOR = readFile('.git/commit-author').trim()

                    println "Git commit id: ${env.GIT_COMMIT_ID}"
                    //println "Git commit author: ${env.GIT_COMMIT_AUTHOR}"

                    // Run maven to build WAR/JAR application
                    if (isUnix()) {
                        sh 'mvn -Dmaven.com.failure.ignore=true -Dtest=!*PasswordConstraintValidatorTest -P jar,release clean package'
                    } else {
                        bat "mvn -Dmaven.com.failure.ignore=true -Dtest=!*PasswordConstraintValidatorTest -P jar,release clean package"
                    }
                }
            }

            post {
                success {
                    // Record the test results (success)
                    junit "**/target/surefire-reports/TEST-*.xml"
                    // Archive the built file
                    archiveArtifacts "target/${env.COMPONENT_NAME}.jar,target/${env.COMPONENT_NAME}.war"
                    // Stash the deployable files
                    stash includes: "target/${env.COMPONENT_NAME}.jar,target/${env.COMPONENT_NAME}.war", name: "${env.COMPONENT_NAME}_release"
                }
                failure {
                    // Record the test results (failures)
                    junit "**/target/surefire-reports/TEST-*.xml"
                }
            }
        }

        stage('SAST') {
            when {
            	beforeAgent true
            	anyOf {
            	    expression { params.SCA_LOCAL == true }
            	    expression { params.SCANCENTRAL_SAST == true }
            	    expression { params.FOD == true }
        	    }
            }
            // Run on an Agent with "fortify" label applied
            agent {label "fortify"}
            steps {
                script {
                    // Get code from Git repository so we can recompile it
                	git credentialsId: 'iwa-git-creds-id', url: "${env.GIT_URL}"

                    // Run Maven debug compile, download dependencies (if required) and package up for FOD
                    if (isUnix()) {
                        sh "mvn -Dmaven.compiler.debuglevel=lines,vars,source -DskipTests -P fortify clean verify"
                        sh "mvn dependency:build-classpath -Dmdep.regenerateFile=true -Dmdep.outputFile=${env.WORKSPACE}/cp.txt"
                    } else {
                        bat "mvn -Dmaven.compiler.debuglevel=lines,vars,source -DskipTests -P fortify clean verify"
                        bat "mvn dependency:build-classpath -Dmdep.regenerateFile=true -Dmdep.outputFile=${env.WORKSPACE}/cp.txt"
                    }

                    // read contents of classpath file
                    def classpath = readFile "${env.WORKSPACE}/cp.txt"
                    println "Using classpath: $classpath"

                    if (params.FOD) {
                        // Upload built application to Fortify on Demand and carry out Static Assessment
                        fodStaticAssessment releaseId: ${env.FOD_RELEASE_ID},
                            // bsiToken: "${env.FOD_BSI_TOKEN}",
                            entitlementPreference: 'SubscriptionOnly',
                            inProgressScanActionType: 'CancelInProgressScan',
                            remediationScanPreferenceType: 'NonRemediationScanOnly',
                            srcLocation: "${env.FOD_UPLOAD_DIR}"

                        // optional: wait for FOD assessment to complete
                        fodPollResults releaseId: ${env.FOD_RELEASE_ID}
                            //bsiToken: "${env.FOD_BSI_TOKEN}",
                            //policyFailureBuildResultPreference: 1,
                            pollingInterval: 5
                    } else if (params.SCANCENTRAL_SAST) {

                        // set any standard remote translation/scan options
                        fortifyRemoteArguments transOptions: '',
                              scanOptions: ''

                        if (params.UPLOAD_TO_SSC) {
                            // Remote analysis (using Scan Central) and upload to SSC
                            fortifyRemoteAnalysis remoteAnalysisProjectType: fortifyMaven(buildFile: 'pom.xml'),
                                remoteOptionalConfig: [
                                    customRulepacks: '',
                                    filterFile: "etc\\sca-filter.txt",
                                    notifyEmail: "${params.SSC_NOTIFY_EMAIL}",
                                    sensorPoolUUID: "${params.SSC_SENSOR_POOL_UUID}"
                                ],
                                uploadSSC: [appName: "${env.APP_NAME}", appVersion: "${env.APP_VER}"]

                        } else {
                            // Remote analysis (using Scan Central)
                            fortifyRemoteAnalysis remoteAnalysisProjectType: fortifyMaven(buildFile: 'pom.xml'),
                                remoteOptionalConfig: [
                                    customRulepacks: '',
                                    filterFile: "etc\\sca-filter.txt",
                                    notifyEmail: "${params.SSC_NOTIFY_EMAIL}",
                                    sensorPoolUUID: "${params.SSC_SENSOR_POOL_UUID}"
                                ]
                        }
                    } else if (params.SCA_LOCAL) {
                        // optional: update scan rules
                        //fortifyUpdate updateServerURL: 'https://update.fortify.com'

                        // Clean project and scan results from previous run
                        fortifyClean buildID: "${env.COMPONENT_NAME}",
                            logFile: "${env.COMPONENT_NAME}-clean.log"

                        // Translate source files
                        fortifyTranslate buildID: "${env.COMPONENT_NAME}",
                            projectScanType: fortifyJava(javaSrcFiles:
                                '\""src/main/java/**/*\"" \""src/main/resources/**/*\""',
                            javaVersion: "${env.JAVA_VERSION}",
                            javaClasspath: "$classpath"),
                            addJVMOptions: '',
                            logFile: "${env.COMPONENT_NAME}-translate.log"

                        // Scan source files
                        fortifyScan buildID: "${env.COMPONENT_NAME}",
                            addOptions: '"-filter" "etc\\sca-filter.txt"',
                            resultsFile: "${env.COMPONENT_NAME}.fpr",
                            addJVMOptions: '',
                            logFile: "${env.COMPONENT_NAME}-scan.log"

                        if (params.UPLOAD_TO_SSC) {
                            // Upload to SSC
                            fortifyUpload appName: "${env.APP_NAME}",
                                appVersion: "${env.APP_VER}",
                                resultsFile: "${env.COMPONENT_NAME}.fpr"
                        }
                    } else {
                        println "No Static Application Security Testing (SAST) to do."
                    }
                }
            }
        }

        stage('OSS') {
            when {
                beforeAgent true
                anyOf {
                    expression { params.SCA_OSS == true }
                }
            }
            // Run on an Agent with "fortify" label applied
            agent {label "fortify"}
            steps {
                script {
                    // run sourceandlibscanner - needs to have been installed and in the path
                    if (isUnix()) {
                        sh "sourceandlibscanner -auto -scan -bt mvn -bf pom.xml -sonatype -iqurl ${params.NEXUS_IQ_URL} -nexusauth ${env.NEXUS_IQ_AUTH_TOKEN} -iqappid IWA -stage build -r iqReport.json -upload -ssc ${params.SSC_URL} -ssctoken ${env.SSC_AUTH_TOKEN} -versionid ${params.SSC_APP_VERSION_ID}"
                    } else {
                        bat(script: "sourceandlibscanner -auto -scan -bt mvn -bf pom.xml -sonatype -iqurl ${params.NEXUS_IQ_URL} -nexusauth ${env.NEXUS_IQ_AUTH_TOKEN} -iqappid IWA -stage build -r iqReport.json -upload -ssc ${params.SSC_URL} -ssctoken ${env.SSC_AUTH_TOKEN} -versionid ${params.SSC_APP_VERSION_ID}")
                    }
                }
            }
        }

        stage('Package') {
            // Run on "master" node
            agent { label 'master' }
            steps {
                script {
                    // unstash the built files
                    unstash name: "${env.COMPONENT_NAME}_release"
                    if (isUnix()) {
                        // Create docker image using JAR file
                        dockerImage = docker.build "${params.DOCKER_ORG}/${env.COMPONENT_NAME}:${env.APP_VER}.${env.BUILD_NUMBER}"
                    } else {
                        // Create docker image using JAR file
                        dockerImage = docker.build("${params.DOCKER_ORG}/${env.COMPONENT_NAME}:${env.APP_VER}.${env.BUILD_NUMBER}", "-f Dockerfile.win .")
                    }
                }
            }
        }

        stage('DAST') {
            when {
            	beforeAgent true
            	anyOf {
            	    expression { params.SCANCENTRAL_DAST == true }
            	    expression { params.FOD == true }
        	    }
            }
            // Run on an Agent with "docker" label applied
            agent {label "docker"}
            steps {
                script {
                    if (params.SCANCENTRAL_DAST) {
                        // check if container is still running and if so stop/remove it
                        if (isUnix()) {
                            sh(script: "docker ps -aq --filter name=iwa-jenkins > container.id")
                            if (fileExists('container.id')) {
                                def existingId = readFile('container.id').trim()
                                if (existingId) {
                                    println "Found existing iwa-jenkins container id: ${existingId} ... deleting..."
                                    sh(script: "docker stop $existingId && docker rm -f $existingId")
                                }
                            }
                        } else {
                            bat(script: "docker ps -aq --filter name=iwa-jenkins > container.id")
                            if (fileExists('container.id')) {
                                def existingId = readFile('container.id').trim()
                                if (existingId) {
                                    println "Found existing iwa-jenkins container id: ${existingId} ... deleting..."
                                    bat(script: "docker stop ${existingId} && docker rm -f ${existingId}")
                                }
                            }
                        }

                        // start docker container
                        println "Starting docker container ${dockerContainerName}"
                        dockerContainer = dockerImage.run("--name ${dockerContainerName} -p 9090:8080")

                        // run ScanCentral DAST scan using groovy script
                        println "Running ScanCentral DAST scan, please wait ..."
                        withCredentials([usernamePassword(credentialsId: 'iwa-edast-auth-id', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                            edastApi = load 'bin/fortify-scancentral-dast.groovy'
                            edastApi.setApiUri("${params.EDAST_URL}")
                            edastApi.setDebug(true)
                            edastApi.authenticate("${USERNAME}", "${PASSWORD}")
                            Integer scanId = edastApi.startScanAndWait("Jenkins initiated scan", "${params.EDAST_CICD}", 5)
                            String scanStatus = edastApi.getScanStatusValue(edastApi.getScanStatusId(scanId))
                            println "ScanCentral DAST scan id: ${scanId} - status: ${scanStatus}"
                        }
					} else if (params.FOD) {
						println "DAST via FOD is not yet implemented."						
                    } else {
                        println "No Dynamic Application Security Testing (DAST) to do."
                    }
                }
            }
        }
        
		// An example manual release checkpoint
        stage('Prepare') {
        	agent { label 'master' }
        	steps {
            	input id: 'Release', 
            		message: 'Ready to Release?', 
            		ok: 'Yes, let\'s go', 
            		submitter: 'admin', 
            		submitterParameter: 'approver'
        	}
        }

        stage('Release') {
            agent { label 'master' }
            steps {
                script {
                    // Example publish to Docker Hub
                    if (params.RELEASE_TO_DOCKERHUB) {
                        docker.withRegistry('https://registry.hub.docker.com', 'iwa-dockerhub-creds-id') {
                            dockerImage.push("${env.APP_VER}.${BUILD_NUMBER}")
                            // and tag as "latest"
                            dockerImage.push("latest")
                        }
                    } else {
                        println "No releasing to do."
                    }
                }
            }
        }

    }

    post {
        always {
            script {
                // check if container is still running and if so stop/remove it
                if (isUnix()) {
                    sh(script: "docker ps -aq --filter name=iwa-jenkins > container.id")
                    if (fileExists('container.id')) {
                        def existingId = readFile('container.id').trim()
                        if (existingId) {
                            println "Found existing iwa-jenkins container id: ${existingId} ... deleting..."
                            sh(script: "docker stop $existingId && docker rm -f $existingId")
                        }
                    }
                } else {
                    bat(script: "docker ps -aq --filter name=iwa-jenkins > container.id")
                    if (fileExists('container.id')) {
                        def existingId = readFile('container.id').trim()
                        if (existingId) {
                            println "Found existing iwa-jenkins container id: ${existingId} ... deleting..."
                            bat(script: "docker stop ${existingId} && docker rm -f ${existingId}")
                        }
                    }
                }
            }
        }
    }

}

def cleanupContainer() {

}

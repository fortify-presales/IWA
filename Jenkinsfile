#!/usr/bin/env groovy

//********************************************************************************
// An example pipeline for DevSecOps using Micro Focus Fortify Application Security products
// @author: Kevin A. Lee (kevin.lee@microfocus.com)
//
//
// Pre-requisites:
// - Fortify SCA has been installed (for on premise SAST) 
// - Fortify WebInspect has been installed (for on premise DAST)
// - A Fortify On Demand account and API access are available (for cloud based SAST)
//
// Node setup:
// - Apply the label "fortify" to Fortify SCA and WebInspect agent.
// Also ensure the label "master" has been applied to your Jenkins master.
//
// Credentials setup:
// Create the following "Secret text" credentials in Jenkins and enter values as follows:
//		iwa-git-creds-id		 - Git login as Jenkins "Username with Password" credential
//      iwa-fod-bsi-token-id     - Fortify on Demand BSI token as Jenkins Secret - deprecated use iwa-fod-release-id
//      iwa-fod-release-id       - Fortify on Demand Release Id as Jenkins Secret credential
//      iwa-ssc-auth-token-id    - Fortify Software Security Center "AnalysisUploadToken" authentication token as Jenkins Secret credential
//      docker-hub-credentials   - DockerHub login as "Username/Password" credentials
// All of the credentials should be created (with empty values if necessary) even if you are not using the capabilities.
// For Fortify on Demand (FOD) Global Authentication should be used rather than Personal Access Tokens.
//
//********************************************************************************

// The instances of Docker image and container that are created
def dockerImage
def dockerContainer

pipeline {
    agent any

    //
    // The following parameters can be selected when the pipeline is executed manually to execute
    // different capabilities in the pipeline.

    // Note: the pipeline needs to be executed at least once for the parameters to be available
    //
    parameters {
        booleanParam(name: 'SAST_SCA',       	defaultValue: false,
            description: 'Use local Fortify SCA for Static Application Security Testing')
        booleanParam(name: 'SAST_SCA_OSS',      defaultValue: false,
            description: 'Use local Fortify SCA and Sonatype Nexus IQ for Static Application Security Testing and Open Source Component Analysis')			
        booleanParam(name: 'SAST_SCANCENTRAL', 	defaultValue: false,
            description: 'Run a remote scan using Scan Central for Static Application Security Testing')       
        booleanParam(name: 'DAST_WEBINSPECT', 	defaultValue: false,
            description: 'Use local WebInspect for Dynamic Application Security Testing')		
        booleanParam(name: 'DAST_SCANCENTRAL', 	defaultValue: false,
            description: 'Run a remote WebInspect scan using Scan Central for Dynamic Application Security Testing')				
        booleanParam(name: 'UPLOAD_TO_SSC',		defaultValue: false,
            description: 'Enable upload of scan results to Fortify Software Security Center')             
        booleanParam(name: 'SAST_FOD',       	defaultValue: false,
            description: 'Enable Fortify on Demand for Static Application Security Testing')
        booleanParam(name: 'DAST_FOD',       	defaultValue: false,
            description: 'Enable Fortify on Demand for Dynamic Application Security Testing')			
    }

    environment {
        //
        // Application settings
        //
		APP_NAME = "IWA (Java)"                      		// Application name
        APP_VER = "1.0"                                     // Application release
        COMPONENT_NAME = "iwa"                              // Component name
        GIT_URL = scm.getUserRemoteConfigs()[0].getUrl()    // Git Repo
        GIT_CREDS = credentials('iwa-git-creds-id')			// Git Credentials
        JAVA_VERSION = 8                                    // Java version to compile as
        APP_URL = "https://localhost:8888"                  // URL of where the application is running using docker
        ISSUE_IDS = ""                                      // List of issues found from commit
        DOCKER_ORG = "mfdemouk"                             // Docker organisation (in Docker Hub) to push released images to

        //
        // Fortify On Demand (FOD) settings
        //
        //FOD_BSI_TOKEN = credentials('iwa-fod-bsi-token-id') // FOD BSI Token - deprecated use FOD_RELEASE_ID
        FOD_RELEASE_ID = credentials('iwa-fod-release-id')  // FOD Release Id
        FOD_UPLOAD_DIR = 'fod'                              // Directory where FOD upload Zip is constructed
       
        //
        // Fortify Static Code Analyzer (SCA) settings
        //
		// Should really set an environment variable in Jenkins similar to this:
        FORTIFY_HOME = "C:\\Micro Focus\\Fortify SCA and Apps 20.2.0"	// Home directory for Fortify SCA on agent

        //
        // Fortify Software Security Center (SSC) settings
        //
        SSC_WEBURL = "http://localhost:8080/ssc"                    // URL of SSC
        SSC_AUTH_TOKEN = credentials('iwa-ssc-auth-token-id')       // Authentication token for SSC
        SSC_SENSOR_POOL_UUID = "00000000-0000-0000-0000-000000000002" // UUID of Scan Central Sensor Pool to use - leave for Default Pool
		SSC_NOTIFY_EMAIL = "test@test.com"							// User to notify with SSC/ScanCentral information
		
        //
        // Fortify WebInspect settings
        //
        WI_API = "http://localhost:8083/webinspect"			// WebInspect API - no authentication assumed
        WI_POLICY_ID = 1008									// WebInspect Scan Policy Id - 1008 = Critical and High
        WI_OUTPUT_FILE = "${env.WORKSPACE}\\wi-iwa.fpr"     // Output file (FPR) to create
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
                        sh 'mvn -Dmaven.com.failure.ignore=true -Dtest=!*FailingTests -P jar,release clean package'
                    } else {
                        bat "mvn -Dmaven.com.failure.ignore=true -Dtest=!*FailingTests -P jar,release clean package"
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

        stage('Package') {
            // Run on "master" node
            agent { label 'master' }
            steps {
                script {
                    // unstash the built files
                    unstash name: "${env.COMPONENT_NAME}_release"
                	if (isUnix()) {
                    	// Create docker image using JAR file
                    	dockerImage = docker.build "${env.DOCKER_ORG}/${env.COMPONENT_NAME}:${env.APP_VER}.${env.BUILD_NUMBER}"
                    } else {
                    	// Create docker image using JAR file
                    	dockerImage = docker.build("${env.DOCKER_ORG}/${env.COMPONENT_NAME}:${env.APP_VER}.${env.BUILD_NUMBER}", "-f Dockerfile.win .")
                    }	
                }
            }
        }

        stage('SAST') {
            when {
            	beforeAgent true
            	anyOf {
            	    expression { params.SAST_SCA == true }
            	    expression { params.SAST_SCANCENTRAL == true }
            	    expression { params.SAST_FOD == true }         	     
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

                    if (params.SAST_FOD) {
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
                    } else if (params.SAST_SCANCENTRAL) {

                        // set any standard remote translation/scan options
                        fortifyRemoteArguments transOptions: '',
                              scanOptions: ''

                        if (params.UPLOAD_TO_SSC) {
                            // Remote analysis (using Scan Central) and upload to SSC
                            fortifyRemoteAnalysis remoteAnalysisProjectType: fortifyMaven(buildFile: 'pom.xml'),
                                remoteOptionalConfig: [
                                    customRulepacks: '',
                                    filterFile: "etc\\sca-filter.txt",
                                    notifyEmail: "${env.SSC_NOTIFY_EMAIL}",
                                    sensorPoolUUID: "${env.SSC_SENSOR_POOL_UUID}"
                                ],
                                uploadSSC: [appName: "${env.APP_NAME}", appVersion: "${env.APP_VER}"]

                        } else {
                            // Remote analysis (using Scan Central)
                            fortifyRemoteAnalysis remoteAnalysisProjectType: fortifyMaven(buildFile: 'pom.xml'),
                                remoteOptionalConfig: [
                                    customRulepacks: '',
                                    filterFile: "etc\\sca-filter.txt",
                                    notifyEmail: "${env.SSC_NOTIFY_EMAIL}",
                                    sensorPoolUUID: "${env.SSC_SENSOR_POOL_UUID}"
                                ]
                        }

                    } else if (params.SAST_SCA) {
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
					} else if (params.SAST_SCA_OSS) {
						println "SAST and OSS via SCA and Sonatype is not yet implemented."
                    } else {
                        println "No Static Application Security Testing (SAST) to do."
                    }
                }
            }
        }

        stage('DAST') {
            when {
            	beforeAgent true
            	anyOf {
            	    expression { params.DAST_WEBINSPECT == true }
            	    expression { params.DAST_SCANCENTRAL == true }
            	    expression { params.DAST_FOD == true }         	     
        	    }
            }
            // Run on an Agent with "fortify" label applied
            agent {label "fortify"}
            steps {
                script {
                    if (params.DAST_WEBINSPECT) {
                        // start docker container
                        dockerContainer = dockerImage.run("-p 8888:8080")

						// run WebInspect scan
						code = load 'etc/wi-scan.groovy'
                        code.runWebInspectScan("${env.WI_API}", "IWA-UI", "IWA Web Scan", "${env.APP_URL}", "Login", 1008)   

                        // stop docker container
                        dockerContainer.stop()
						if (params.UPLOAD_TO_SSC) {
							println "DAST Upload to SSC is not yet implemented"
						}
					} else if (params.DAST_SCANCENTRAL) {
						println "DAST via ScanCentral is not yet implemented."
					} else if (params.DAST_FOD) {
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
                    docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                        dockerImage.push("${env.APP_VER}.${BUILD_NUMBER}")
                        // and tag as "latest"
                        dockerImage.push("latest")
                    }
                }
            }
        }
    }
}

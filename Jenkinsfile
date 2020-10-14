//********************************************************************************
// An example pipeline for DevSecOps using Micro Focus Fortify and Deployment Automation products
// @author: Kevin A. Lee (kevin.lee@microfocus.com)
//
//
// Pre-requisites:
// - Fortify SCA has been installed (for on-premise static analysis) 
// - Fortify WebInspect has been installed (for on-premise dynamic analysis)
// - A Fortify On Demand account and API access are available (for cloud based static analysis)
// Optional:
// - WebSphere Liberty has been installed (for automated deployment of WAR file)
//
// Node setup:
// - For Fortify on-premise software: Apply the label "fortify" to Fortify SCA agent and "webinspect" to the WebInspect agent.
// - For Fortify on Demand: apply the label "fortify" to the agent or master that will connect to Fortify on Demand.
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
        booleanParam(name: 'SCA_ENABLED',       defaultValue: false,
            description: 'Enable Fortify SCA for Static Application Security Testing')
        booleanParam(name: 'FOD_ENABLED',       defaultValue: false,
            description: 'Enable Fortify on Demand for Static Application Security Testing')
        booleanParam(name: 'SSC_ENABLED',       defaultValue: false,
            description: 'Enable upload of scans to Fortify Software Security Center')
        booleanParam(name: 'SCANCENTRAL_ENABLED', defaultValue: false,
            description: 'Run a remote scan via Scan Central')
        booleanParam(name: 'WEBINSPECT_ENABLED', defaultValue: false,
            description: 'Enable WebInspect for Dynamic Application Security Testing')
        booleanParam(name: 'WLP_ENABLED',        defaultValue: false,
            description: 'Deploy WAR file to WebSphere Liberty Profile server')
        booleanParam(name: 'DOCKER_ENABLED',    defaultValue: false,
            description: 'Use Docker for automated deployment of JAR file into container')
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
        APP_WEBURL = "https://localhost:6443/iwa/"          // URL of where the application is deployed to (for integration testing, WebInspect etc)
        ISSUE_IDS = ""                                      // List of issues found from commit
        DOCKER_ORG = "mfdemouk"                             // Docker organisation (in Docker Hub) to push images to

        //
        // Fortify On Demand (FOD) settings
        //
        //FOD_BSI_TOKEN = credentials('iwa-fod-bsi-token-id') // FOD BSI Token - deprecated use FOD_RELEASE_ID
        FOD_RELEASE_ID = credentials('iwa-fod-release-id')  // FOD Release Id
        FOD_UPLOAD_DIR = 'fod'                              // Directory where FOD upload Zip is constructed
       
        //
        // Fortify Static Code Analyzer (SCA) settings
        //
        FORTIFY_HOME = "C:\\Micro Focus\\Fortify SCA and Apps 20.1.2"	// Home directory for Fortify SCA on agent

        //
        // Fortify Software Security Center (SSC) settings
        //
        SSC_WEBURL = "http://localhost:8080/ssc"                    // URL of SSC
        SSC_AUTH_TOKEN = credentials('iwa-ssc-auth-token-id')       // Authentication token for SSC
        SSC_SENSOR_POOL_UUID = "00000000-0000-0000-0000-000000000002" // UUID of Scan Central Sensor Pool to use - leave for Default Pool
		SSC_NOTIFIY_EMAIL = "test@test.com"							// User to notify with SSC/ScanCentral information
		
        //
        // Fortify WebInspect settings
        //
        WI_CLIENT_PATH = "C:\\Micro Focus\\Fortify WebInspect\\WI.exe"  // Path to WebInspect executable on the "webinspect" Agent
        WI_SETTINGS_FILE = "${env.WORKSPACE}\\etc\\WebScanSettings.xml" // Settings file to run
        WI_LOGIN_MACRO = "${env.WORKSPACE}\\etc\\Login.webmacro"        // Login macro to use
        WI_OUTPUT_FILE = "${env.WORKSPACE}\\wi-iwa.fpr"      			// Output file (FPR) to create
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
                        if (params.DOCKER_ENABLED) {
                            sh 'mvn -Dmaven.com.failure.ignore=true -Dtest=!*FailingTests -P jar,release clean package'
                        } else {
                            sh 'mvn -Dmaven.com.failure.ignore=true -Dtest=!*FailingTests -P war,release clean package'
                        }
                    } else {
                        if (params.DOCKER_ENABLED) {
                            bat "mvn -Dmaven.com.failure.ignore=true -Dtest=!*FailingTests -P jar,release clean package"
                        } else {
                            bat "mvn -Dmaven.com.failure.ignore=true -Dtest=!*FailingTests -P war,release clean package"
                        }
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
                    if (params.WLP_ENABLED) {
						// WAR file should already exist
                    } else if (params.DOCKER_ENABLED) {
                        // Create docker image using JAR file
                        dockerImage = docker.build "${env.DOCKER_ORG}/${env.COMPONENT_NAME}:${env.APP_VER}.${env.BUILD_NUMBER}"
                    } else {
                        println "No packaging to do ..."
                    }
                }
            }
        }

        stage('SAST') {
            when {
            	beforeAgent true
            	anyOf {
            	    expression { params.SCA_ENABLED == true }
            	    expression { params.SCANCENTRAL_ENABLED == true }
            	    expression { params.FOD_ENABLED == true }         	     
        	    }
            }
            // Run on an Agent with "fortify" label applied - assumes Fortify SCA command line tools are installed
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

                    if (params.FOD_ENABLED) {
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
                    } else if (params.SCANCENTRAL_ENABLED) {

                        // set any standard remote translation/scan options
                        fortifyRemoteArguments transOptions: '',
                              scanOptions: ''

                        if (params.SSC_ENABLED) {
                            // Remote analysis (using Scan Central) and upload to SSC
                            fortifyRemoteAnalysis remoteAnalysisProjectType: fortifyMaven(buildFile: 'pom.xml'),
                                remoteOptionalConfig: [
                                    customRulepacks: '',
                                    filterFile: "etc\\sca-filter.txt",
                                    notifyEmail: '${env.SSC_NOTIFIY_EMAIL}',
                                    sensorPoolUUID: "${env.SSC_SENSOR_POOL_UUID}"
                                ],
                                uploadSSC: [appName: "${env.APP_NAME}", appVersion: "${env.APP_VER}"]

                        } else {
                            // Remote analysis (using Scan Central)
                            fortifyRemoteAnalysis remoteAnalysisProjectType: fortifyMaven(buildFile: 'pom.xml'),
                                remoteOptionalConfig: [
                                    customRulepacks: '',
                                    filterFile: '"-filter" "etc\\sca-filter.txt',
                                    notifyEmail: '${env.SSC_NOTIFIY_EMAIL}',
                                    sensorPoolUUID: "${env.SSC_SENSOR_POOL_UUID}"
                                ]
                        }

                    } else if (params.SCA_ENABLED) {
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

                        if (params.SSC_ENABLED) {
                            // Upload to SSC
                            fortifyUpload appName: "${env.APP_NAME}",
                                appVersion: "${env.APP_VER}",
                                resultsFile: "${env.COMPONENT_NAME}.fpr"
                        }
                    } else {
                        println "No Static Application Security Testing (SAST) to do  ..."
                    }
                }
            }
        }

        stage('Deploy') {
            when {
            	beforeAgent true
            	anyOf {
            	    expression { params.DOCKER_ENABLED == true }
            	    expression { params.params.WLP_ENABLED == true }         	     
        	    }
            }
            // Run on "master" node
            agent { label 'master' }
            steps {
                 script {
                	unstash name: "${env.COMPONENT_NAME}_release"                        
                    if (params.DOCKER_ENABLED) {
                        // Run Docker container
                        dockerContainer = dockerImage.run()
                    } else { // if (params.WLP_ENABLED) {
	                	// Start WebSphere Liberty server integration instance  
	                	if (isUnix()) {
	                    	sh "mvn -Pwlp.int liberty:create liberty:install-feature liberty:deploy liberty:start"
	                    } else {	
	                		bat "mvn -Pwlp.int liberty:create liberty:install-feature liberty:deploy liberty:start"
	                	}	
                    }
                 }
             }
        }

        stage('DAST') {
        	when {
            	beforeAgent true
        	    expression { params.WEBINSPECT_ENABLED == true }
            }
            // Run on an Agent with "webinspect" label applied - assumes WebInspect command line installed
            // Needs JDK and Maven installed
            agent {label "webinspect"}
            steps {
                script {
                    if (params.WEBINSPECT_ENABLED) {
                        // Run WebInspect on deployed application and upload to SSC
                        if (isUnix()) {
                            println "Sorry, WebInspect is only supported on Windows..."
                        } else {     
                            // Run WebInspect "Critial and High" policy
                            bat(/"${env.WI_CLIENT_PATH}" -s "${env.WI_SETTINGS_FILE}" -macro "${env.WI_LOGIN_MACRO}" -u "${env.APP_WEBURL}" -ep "${env.WI_OUTPUT_FILE}" -ps 1008 & EXIT \/B 0/)
                            if (params.FOD_ENABLED) {
                                //TODO: upload FPR to FOD
                            } else if (params.SSC_ENABLED) {
                                bat(/fortifyclient.bat uploadFPR -f "${env.WI_OUTPUT_FILE}" -url "${env.SSC_WEBURL}" -authtoken "${env.SSC_AUTH_TOKEN}" -application "${env.APP_NAME}" -applicationVersion "${env.APP_VER}"/)
                            }
                        }
                    } else {
                        println "No Dynamic Application Security Testing (DAST) to do ...."
                    }
                }
            }
        }
        
        stage('Prepare') {
        	agent { label 'master' }
        	steps {
        		script {
        		    if (params.WLP_ENABLED) {
                        // Stop WebSphere Liberty server integration instance
                        if (isUnix()) {
                            sh "mvn -Pwlp.int liberty:stop"
                        } else {
                            bat("mvn -Pwlp.int liberty:stop")
                        }
                    }
        		}
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
                    if (params.WLP_ENABLED) {
                    	unstash name: "${env.COMPONENT_NAME}_release"      
                		// release to "next" liberty server, i.e. test/productions                    	                  
                    	if (isUnix()) {
                        	sh "mvn -Pwlp.prod liberty:stop liberty:deploy liberty:start"
                    	} else {
	                    	bat("mvn -Pwlp.prod liberty:stop liberty:deploy liberty:start")
                    	}
                    } else if (params.DOCKER_ENABLED) {
                        // Stop the container if still running
                        dockerContainer.stop()
                        // Example publish to Docker Hub
                        docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                            dockerImage.push("${env.APP_VER}.${BUILD_NUMBER}")
                            // and tag as "latest"
                            dockerImage.push("latest")
                        }
                    } else {
                       	println "No release-ing to do..."
                    }
                }
            }
        }
    }
}
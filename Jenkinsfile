import groovy.json.JsonOutput

pipeline {
    agent any

    //
    // The following parameters can be selected when the pipeline is executed manually to execute
    // different capabilities in the pipeline. Note: the pipeline needs to be executed at least once
    // for the parameters to be available
    //
    parameters {
        booleanParam(name: 'SCA_ENABLED',       defaultValue: false,
            description: 'Enable Fortify SCA for Static Application Security Testing')
        booleanParam(name: 'FOD_ENABLED',       defaultValue: false,
            description: 'Enable Fortify on Demand for Static Application Security Testing')
        booleanParam(name: 'SSC_ENABLED',       defaultValue: false,
            description: 'Enable upload of scans to Fortify Software Security Center')
        booleanParam(name: 'WI_ENABLED',        defaultValue: false,
            description: 'Enable WebInspect for Dynamic Application Security Testing')
        booleanParam(name: 'DA_ENABLED',        defaultValue: false,
            description: 'Enable Deployment Automation for automated application deployment')
        booleanParam(name: 'DOCKER_ENABLED',    defaultValue: false,
            description: 'Package up application into Docker image for deployment')
    }

    //
    // Create the following "Secret text" credentials in Jenkins and enter values as follows:
    //      jenkins-fod-bsi-token-id    - Fortify on Demand BSI token
    //      jenkins-ssc-auth-token-id   - Fortify Software Security Center "ArtifactUpload" authentication token
    //      jenkins-da-auth-token-id    - Deployment Automation authentication token
    // For Fortify on Demand (FOD) Global Authentication should be setup
    environment {
        //
        // Application settings
        //
        APP_NAME = "Simple Secure App"                      // Application name
        APP_VER = "1.0"                                     // Application release
        COMPONENT_NAME = "secure-web-app"                   // Component name
        GIT_URL = scm.getUserRemoteConfigs()[0].getUrl()    // Git Repo
        JAVA_VERSION = 8                                    // Java version to compile as
        APP_WEBURL = "http://localhost:8881/secure-web-app/" // URL of where the application is deploy to (for integration testing, WebInspect etc)
        JETTY_BASE_DIR = "C:\\Tools\\jetty\\integration" // Directory where WAR file is deployed to for Jetty based deployment
        ISSUE_IDS = ""                                      // List of issues found from commit

        //
        // Fortify On Demand (FOD) settings
        //
        FOD_BSI_TOKEN = credentials('jenkins-fod-bsi-token-id')     // FOD BSI Token
        FOD_UPLOAD_DIR = 'fod'                                      // Directory where FOD upload Zip is constructed

        //
        // Micro Focus Deployment Automation (DA) settings
        // Download Jenkins plugin from: https://community.microfocus.com/dcvta86296/board/message?board.id=DA_Plugins&message.id=15#M15)
        //
        DA_SITE = "localhost-release"                               // DA Site Name (in Jenkins->Configuration)
        DA_WEBURL = "http://localhost:8080/da"                      // URL of Micro Focus Deployment Automation
        DA_USERNAME = "admin"                                       // User to login to DA as
        DA_AUTH_TOKEN = credentials('jenkins-da-auth-token-id')     // Authentication token for the user
        DA_CLIENT_PATH = "C:\\Micro Focus\\Deployment Automation Client\\da-client.cmd"
        DA_DEPLOY_PROCESS = "Deploy Web App"                        // Deployment process to use
        DA_ENV_NAME = "Systems Integration"                         // Deployment automation environment name

        //
        // Fortify Static Code Analyzer (SCA) settings
        //
        SCA_CLIENT_PATH = "C:\\Micro Focus\\Fortify_SCA_and_Apps_19.2.0\\bin\\fortifyclient.bat"   // Path to "fortifyclient.bat" on the Server/Agent

        //
        // Fortify Software Security Center (SSC) settings
        //
        SSC_WEBURL = "http://localhost:8080/ssc"                    // URL of SSC
        SSC_AUTH_TOKEN = credentials('jenkins-ssc-auth-token-id')   // Authentication token for SSC

        //
        // Fortify WebInspect settings
        //
        WI_CLIENT_PATH = "C:\\Micro Focus\\Fortify WebInspect\\WI.exe"  // Path to WebInspect executable on the Server/Agent
        WI_SETTINGS_FILE = "${env.WORKSPACE}\\etc\\DefaultSettings.xml" // Settings file to run
        WI_LOGIN_MACRO = "${env.WORKSPACE}\\etc\\Login.webmacro"        // Login macro to use
        WI_OUTPUT_FILE = "${env.WORKSPACE}\\wi-secure-web-app.fpr"      // Output file (FPR) to create
    }

    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven 'M3'
        // Install the Git version configure as "Default" an add it to the path.
        git 'Default'
    }

    stages {
        stage('Build') {
            steps {
                // Get some code from a GitHub repository
                git "${env.GIT_URL}"

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
                }

                println "Git commit id: ${env.GIT_COMMIT_ID}"
                //println "Git commit author: ${env.GIT_COMMIT_AUTHOR}"

                // Run maven to build application
                script {
                    if (isUnix()) {
                        sh 'mvn -Dmaven.com.failure.ignore=true -Dtest=!*FailingTests -P war clean package'
                    } else {
                        bat "mvn -Dmaven.com.failure.ignore=true -Dtest=!*FailingTests -P war clean package"
                    }
                }
            }

            post {
                success {
                    // Record the test results (success)
                    junit "**/target/surefire-reports/TEST-*.xml"
                    // Archive the built file
                    archiveArtifacts "target/${env.COMPONENT_NAME}.war"
                    // Stash the deployable files
                    stash includes: "target/${env.COMPONENT_NAME}.war", name: "${env.COMPONENT_NAME}_release"
                }
                failure {
                    // Record the test results (failures)
                    junit "**/target/surefire-reports/TEST-*.xml"
                }
            }

        }

        stage('Package') {
            steps {
                script {
                    // unstash the built files
                    unstash name: "${env.COMPONENT_NAME}_release"
                    if (params.DA_ENABLED) {
                            def verProperties =
                                """job.url=${env.BUILD_URL}
                                jenkins.url=${env.JENKINS_URL}
                                commit.id=${env.GIT_COMMIT_ID}
                                issueIds=${env.ISSUE_IDS}"""
                            step([$class: 'SerenaDAPublisher',
                    			siteName: "${env.DA_SITE}",
                    			component: "${env.COMPONENT_NAME}",
                    			baseDir: "${env.WORKSPACE}/target",
                    			versionName: "${env.APP_VER}.${env.BUILD_NUMBER}",
                    			fileIncludePatterns: "${env.COMPONENT_NAME}.war",
                    			fileExcludePatterns: "**/*tmp*,**/.git",
                    			versionProps: "${verProperties}",
                    			skip: false,
                    			addStatus: true,
                    			statusName: 'BUILT',
                    			deploy: false, // we will deploy later after SAST
                    			deployIf: 'false',
                    			deployUpdateJobStatus: true,
                    			deployApp: "${env.APP_NAME}",
                    			deployEnv: "Systems Integration",
                    			deployProc: "${env.DA_DEPLOY_PROCESS}",
                    			deployProps: "${verProperties}"
                    		])
                        // upload build files into Deployment Automation component version
                        /*if (isUnix()) {
                            sh('"${env.DA_CLIENT_PATH}" --weburl "${env.DA_WEBURL}" --authtoken "${env.DA_AUTH_TOKEN}" createVersion --component "${env.COMPONENT_NAME}" --name "${env.APP_VER}-${BUILD_NUMBER}"')
                            sh('"${env.DA_CLIENT_PATH}" --weburl "${env.DA_WEBURL}" --authtoken "${env.DA_AUTH_TOKEN}" addVersionFiles --component "${env.COMPONENT_NAME}" --version "${env.APP_VER}-${BUILD_NUMBER}" --base "${WORKSPACE}/target" --include "${env.COMPONENT_NAME}.war"')
                            sh('"${env.DA_CLIENT_PATH}" --weburl "${env.DA_WEBURL}" --authtoken "${env.DA_AUTH_TOKEN}" addVersionStatus --component "${env.COMPONENT_NAME}" --version "${env.APP_VER}-${BUILD_NUMBER}" --status "BUILT"')
                        } else {
                            bat(/"${env.DA_CLIENT_PATH}" --weburl "${env.DA_WEBURL}" --authtoken "${env.DA_AUTH_TOKEN}" createVersion --component "${env.COMPONENT_NAME}" --name "${env.APP_VER}-${BUILD_NUMBER}"/)
                            bat(/"${env.DA_CLIENT_PATH}" --weburl "${env.DA_WEBURL}" --authtoken "${env.DA_AUTH_TOKEN}" addVersionFiles --component "${env.COMPONENT_NAME}" --version "${env.APP_VER}-${BUILD_NUMBER}" --base "${WORKSPACE}\\target" --include "${env.COMPONENT_NAME}.war"/)
                            bat(/"${env.DA_CLIENT_PATH}" --weburl "${env.DA_WEBURL}" --authtoken "${env.DA_AUTH_TOKEN}" addVersionStatus --component "${env.COMPONENT_NAME}" --version "${env.APP_VER}-${BUILD_NUMBER}" --status "BUILT"/)
                        }*/
                    }
                }
            }
        }

        stage('SAST') {
            // Run on an Agent with "fortify" label applied - assumes Fortify SCA command line tools are installed
            agent {label "fortify"}
            steps {
                // Get code from Git repository
                git "${env.GIT_URL}"

                script {
                    // Run Maven debug compile, download dependencies (if required) and package up for FOD
                    if (isUnix()) {
                        sh 'mvn -Dmaven.compiler.debuglevel=lines,vars,source -DskipTests clean verify'
                    } else {
                        bat "mvn -Dmaven.compiler.debuglevel=lines,vars,source -DskipTests clean verify"
                    }

                    if (params.FOD_ENABLED) {
                        // Upload built application to Fortify on Demand and carry out Static Assessment
                        fodStaticAssessment bsiToken: "${env.FOD_BSI_TOKEN}",
                            entitlementPreference: 'SubscriptionOnly',
                            inProgressScanActionType: 'CancelInProgressScan',
                            remediationScanPreferenceType: 'NonRemediationScanOnly',
                            srcLocation: "${env.FOD_UPLOAD_DIR}"

                        // optional: wait for FOD assessment to complete
                        fodPollResults bsiToken: "${env.FOD_BSI_TOKEN}",
                            //policyFailureBuildResultPreference: 1,
                            pollingInterval: 5
                    } else if (params.SCA_ENABLED) {
                        // optional: update scan rules
                        //fortifyUpdate updateServerURL: 'https://update.fortify.com'

                        // Clean project and scan results from previous run
                        fortifyClean buildID: "${env.COMPONENT_NAME}",
                            logFile: "${env.COMPONENT_NAME}-clean.log"

                        // Translate source files
                        fortifyTranslate buildID: "${env.COMPONENT_NAME}",
                            projectScanType: fortifyJava(javaSrcFiles:
                                '\""src/main/java/**/*.java\"" \""src/main/resources/**/*.html\""',
                                javaVersion: "${env.JAVA_VERSION}"),
                            logFile: "${env.COMPONENT_NAME}-translate.log"

                        // optional: translate directly using Maven
                        //fortifyTranslate buildID: "${env.COMPONENT_NAME}",
                        //    projectScanType: fortifyMaven3(mavenOptions: "-Dmaven.compiler.debuglevel=lines,vars,source -DskipTests clean verify"),
                        //    logFile: "${env.COMPONENT_NAME}-translate.log"

                        // Scan source files
                        fortifyScan buildID: "${env.COMPONENT_NAME}",
                            addOptions: '"-filter" "etc\\sca-filter.txt"',
                            resultsFile: "${env.COMPONENT_NAME}.fpr",
                            logFile: "${env.COMPONENT_NAME}-scan.log"

                        if (params.SSC_ENABLED) {
                            // Upload to SSC
                            fortifyUpload appName: "${env.APP_NAME}",
                                appVersion: "${env.APP_VER}",
                                resultsFile: "${env.COMPONENT_NAME}.fpr"
                        }

                        if (params.DA_ENABLED) {
                            step([$class: 'UpdateComponentVersionStatusNotifier',
                                siteName: "${env.DA_SITE}",
                                action: 'ADD',
                                componentName: "${env.COMPONENT_NAME}",
                                versionName: "${env.APP_VER}-${BUILD_NUMBER}",
                                statusName: 'SAST'
                            ])
                        }
                    } else {
                        println "Skipping Static Application Security Testing..."
                    }
                }
            }
        }

        stage('Deploy') {
             steps {
                 script {
                    if (params.DA_ENABLED) {
                        def procProperties =
                            """job.url=${env.JOB_URL}
                            jenkins.url=${env.JENKINS_URL}"""
                        step([$class: 'RunApplicationProcessNotifier',
                                siteName: "${env.DA_SITE}",
                                runApplicationProcessIf: 'true',
                                updateJobStatus: true,
                                applicationName: "${env.APP_NAME}",
                                environmentName: "${env.DA_ENV_NAME}",
                                applicationProcessName: "${env.DA_DEPLOY_PROCESS}",
                                componentName: "${env.COMPONENT_NAME}",
                                versionName: "${env.APP_VER}-${BUILD_NUMBER}",
                                applicationProcessProperties: "${procProperties}"
                        ])
                        /*def data = [
                           application: "${env.APP_NAME}",
                           applicationProcess : "${env.DA_DEPLOY_PROCESS}",
                           environment : "Systems Integration",
                           properties: [
                               [
                                   "jenkins.url": "${env.JENKINS_URL}",
                                   "job.url": "${env.JOB_URL}"
                               ]
                           ],
                           versions: [
                               [
                                   version: "${env.APP_VER}-${BUILD_NUMBER}",
                                   component: "${env.COMPONENT_NAME}"
                               ]
                           ]
                        ]

                        def json = JsonOutput.toJson(data)
                        def file = new File("${WORKSPACE}/da-process.json")
                        file.write(JsonOutput.prettyPrint(json))

                        // deploy to Integration environment using Deployment Automation
                        if (isUnix()) {
                            sh('"${env.DA_CLIENT_PATH}" --weburl "${env.DA_WEBURL}" --authtoken "${env.DA_AUTH_TOKEN}" requestApplicationProcess "${WORKSPACE}/da-process.json"')
                        } else {
                            bat(/"${env.DA_CLIENT_PATH}" --weburl "${env.DA_WEBURL}" --authtoken "${env.DA_AUTH_TOKEN}" requestApplicationProcess "${WORKSPACE}\\da-process.json"/)
                        }*/
                    } else {
                        // unstash the built files
                        unstash name: "${env.COMPONENT_NAME}_release"
                        // and hot deploy into Jetty "webapps" directory
                        // requires "File Operations" plugin
                        fileOperations([fileCopyOperation(
                            includes: "target/${env.COMPONENT_NAME}.war",
                            excludes: "",
                            flattenFiles: true, renameFiles: false,
                            sourceCaptureExpression: "",
                            targetLocation: "${env.JETTY_BASE_DIR}/webapps", targetNameExpression: ""
                            )])
                    }
                 }
             }
        }

        stage('DAST') {
            // Run on an Agent with "webinspect" label applied - assumes WebInspect command line installed
            agent {label "webinspect"}
            steps {
                script {
                    // start the application?
                    if (params.WI_ENABLED) {
                        sleep time: 5, unit: 'MINUTES' // wait 5 minutes for application to be ready?
                        // Run WebInspect on deployed application and upload to SSC
                        if (isUnix()) {
                            println "Sorry, WebInspect is only supported on Windows..."
                        } else {
                            bat(/"${env.WI_CLIENT_PATH}" -s "${env.WI_SETTINGS_FILE}" -macro "${env.WI_LOGIN_MACRO}" -u "${env.APP_WEBURL}" -ep "${env.WI_OUTPUT_FILE}"/)
                            if (params.FOD_ENABLED) {
                                //TODO: upload FPR to FOD
                            } else if (params.SSC_ENABLED) {
                                bat(/"${env.SCA_CLIENT_PATH}" uploadFPR -f "${env.WI_OUTPUT_FILE}" -url "${env.SSC_WEBURL}" -authtoken "${env.SSC_AUTH_TOKEN}" -application "${env.APP_NAME}" -applicationVersion "${env.APP_VER}"/)
                            }
                        }
                    } else {
                        println "Skipping Dynamic Application Security Testing...."
                    }
                }
            }
        }

        stage('Release') {
            steps {
                script {
                    // Deploy to the "release" environment using Deployment Automation
                    if (params.DA_ENABLED) {
						println "..."
                    } else {
                        println "Released..."
                    }
                }
            }
        }
    }
}

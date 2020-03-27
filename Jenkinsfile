import groovy.json.JsonOutput

pipeline {
    agent any

    //
    // The following parameters can be selected when the pipeline is executed manually to configure
    // optional capabilities in the pipeline.
    // Note: the pipeline needs to be executed at least once for the parameters to be available
    //
    parameters {
        booleanParam(name: 'SCA_ENABLED', defaultValue: true,
            description: 'Enable Fortify SCA for Static Application Security Testing')
        booleanParam(name: 'FOD_ENABLED', defaultValue: true,
            description: 'Enable Fortify on Demand for Static Application Security Testing')
        booleanParam(name: 'SSC_ENABLED', defaultValue: true,
            description: 'Enable upload of scans to Fortify Software Security Center')
        booleanParam(name: 'WI_ENABLED',  defaultValue: true,
            description: 'Enable WebInspect for Dynamic Application Security Testing')
        booleanParam(name: 'DA_ENABLED',  defaultValue: true,
            description: 'Enable Deployment Automation for automated application deployment')
    }

    //
    // Create the following "Secret text" credentials in Jenkins and enter values as follows:
    //      jenkins-fod-username-id     - Fortify on Demand username
    //      jenkins-fod-bsi-token-id    - Fortify on Demand BSI token
    //      jenkins-ssc-auth-token-id   - Fortify Software Security Center "ArtifactUpload" authentication token
    //      jenkins-da-auth-token-id    - Deployment Automation authentication token
    // Create the following "Personal Access Tokens" in Jenkins and enter values as follows:
    //      FODPAT                      - Fortify on Demand Personal Access Token
    //
    environment {
        GIT_URL = scm.getUserRemoteConfigs()[0].getUrl()
        APP_NAME = "Simple Secure App"
        APP_VER = "1.0"
        APP_WEBURL = "http://localhost:8881/secure-web-app/"
        JAVA_VERSION = 8
        FOD_BSI_TOKEN = credentials('jenkins-fod-bsi-token-id')
        FOD_PAT = 'FODPAT'
        FOD_USERNAME = credentials('jenkins-fod-username-id')
        FOD_TENANT_ID = 'emeademo'
        FOD_UPLOAD_DIR = 'fod'
        COMPONENT_NAME = "secure-web-app"
        DA_USERNAME = "admin"
        DA_AUTH_TOKEN = credentials('jenkins-da-auth-token-id')
        DA_WEBURL = "http://localhost:8080/da"
        // Path to Deployment Automation Client on the Build Agent
        DA_CLIENT_PATH = "C:\\Micro Focus\\Deployment Automation Client\\da-client.cmd"
        DA_DEPLOY_PROCESS = "Deploy Web App"
        // Path to fortifyclient.bat on the Server/Agent
        SCA_CLIENT_PATH = "C:\\Micro Focus\\Fortify_SCA_and_Apps_19.2.0\\bin\\fortifyclient.bat"
        SSC_WEBURL = "http://localhost:8080/ssc"
        SSC_AUTH_TOKEN = credentials('jenkins-ssc-auth-token-id')
        // Path to WebInspect executable on the Server/Agent
        WI_CLIENT_PATH = "C:\\Micro Focus\\Fortify WebInspect\\WI.exe"
        WI_SETTINGS_FILE = "${env.WORKSPACE}\\etc\\DefaultSettings.xml"
        WI_LOGIN_MACRO = "${env.WORKSPACE}\\etc\\Login.webmacro"
        WI_OUTPUT_FILE = "${env.WORKSPACE}\\wi-secure-web-app.fpr"
        JETTY_BASE_DIR = "C:\\Tools\\jetty\\integration"
    }

    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "M3"
        // Install the Git version configure as "GIT" an add it to the path.
        git "GIT"
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

                //println "Git commit id: ${env.GIT_COMMIT_ID}"
                //println "Git commit author: ${env.GIT_COMMIT_AUTHOR}"

                // Run maven to build application
                script {
                    if (isUnix()) {
                        sh 'mvn -Dmaven.com.failure.ignore=true -Dtest=!*FailingTests clean package'
                    } else {
                        bat "mvn -Dmaven.com.failure.ignore=true -Dtest=!*FailingTests clean package"
                    }
                }
            }

            post {
                success {
                    // Record the test results (success)
                    junit "**/target/surefire-reports/TEST-*.xml"
                    // Archive the built file
                    archiveArtifacts "target/${env.COMPONENT_NAME}.war"

                    script {
                        if (params.DA_ENABLED) {
                            // upload build files into Deployment Automation component version
                            if (isUnix()) {
                                sh('"${env.DA_CLIENT_PATH}" --weburl "${env.DA_WEBURL}" --authtoken "${env.DA_AUTH_TOKEN}" createVersion --component "${env.COMPONENT_NAME}" --name "${env.APP_VER}-${BUILD_NUMBER}"')
                                sh('"${env.DA_CLIENT_PATH}" --weburl "${env.DA_WEBURL}" --authtoken "${env.DA_AUTH_TOKEN}" addVersionFiles --component "${env.COMPONENT_NAME}" --version "${env.APP_VER}-${BUILD_NUMBER}" --base "${WORKSPACE}/target" --include "${env.COMPONENT_NAME}.war"')
                                sh('"${env.DA_CLIENT_PATH}" --weburl "${env.DA_WEBURL}" --authtoken "${env.DA_AUTH_TOKEN}" addVersionStatus --component "${env.COMPONENT_NAME}" --version "${env.APP_VER}-${BUILD_NUMBER}" --status "BUILT"')
                            } else {
                                bat(/"${env.DA_CLIENT_PATH}" --weburl "${env.DA_WEBURL}" --authtoken "${env.DA_AUTH_TOKEN}" createVersion --component "${env.COMPONENT_NAME}" --name "${env.APP_VER}-${BUILD_NUMBER}"/)
                                bat(/"${env.DA_CLIENT_PATH}" --weburl "${env.DA_WEBURL}" --authtoken "${env.DA_AUTH_TOKEN}" addVersionFiles --component "${env.COMPONENT_NAME}" --version "${env.APP_VER}-${BUILD_NUMBER}" --base "${WORKSPACE}\\target" --include "${env.COMPONENT_NAME}.war"/)
                                bat(/"${env.DA_CLIENT_PATH}" --weburl "${env.DA_WEBURL}" --authtoken "${env.DA_AUTH_TOKEN}" addVersionStatus --component "${env.COMPONENT_NAME}" --version "${env.APP_VER}-${BUILD_NUMBER}" --status "BUILT"/)
                            }
                        } else {
                            // just stash the built file for now
                            stash includes: "target/${env.COMPONENT_NAME}.war", name: "${env.COMPONENT_NAME}_release"
                        }
                    }
                }
                failure {
                    // Record the test results (failures)
                    junit "**/target/surefire-reports/TEST-*.xml"
                }
            }

        }

        stage('Verification') {
            parallel {
                stage('SAST') {
                    agent {label "fortify"}
                    steps {
                        // Get some code from a GitHub repository
                        git "${env.GIT_URL}"

                        script {
                            // Run Maven debug compile and download dependencies
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
                                    overrideGlobalConfig: true,
                                    personalAccessToken: "${env.FOD_PAT}",
                                    remediationScanPreferenceType: 'NonRemediationScanOnly',
                                    srcLocation: "${env.FOD_UPLOAD_DIR}",
                                    tenantId: "${env.FOD_TENANT_ID}",
                                    username: "${env.FOD_USERNAME}"

                                // optional: wait for FOD assessment to complete
                                fodPollResults bsiToken: "${env.FOD_BSI_TOKEN}",
                                    overrideGlobalConfig: true,
                                    personalAccessToken: "${env.FOD_PAT}",
                                    //policyFailureBuildResultPreference: 1,
                                    pollingInterval: 5,
                                    tenantId: "${env.FOD_TENANT_ID}",
                                    username: "${env.FOD_USERNAME}"
                            } else if (params.SCA_ENABLED){
                                // Update scan rules
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

                                // Scan source files
                                fortifyScan buildID: "${env.COMPONENT_NAME}",
                                    resultsFile: "${env.COMPONENT_NAME}.fpr",
                                    logFile: "${env.COMPONENT_NAME}-scan.log"

                                if (params.SSC_ENABLED) {
                                    // Upload to SSC
                                    fortifyUpload appName: "${env.APP_NAME}",
                                        appVersion: "${env.APP_VER}",
                                        resultsFile: "${env.COMPONENT_NAME}.fpr"
                                }
                            } else {
                                println "Skipping static application security testing..."
                            }
                        }
                    }
                }
                stage('Deploy') {
                     steps {
                         script {
                            if (params.DA_ENABLED) {
                                println "Deploying application using Deployment Automation..."
                                def data = [
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
                                }
                            } else {
                                // unstash the built files
                                unstash includes: name: "${env.COMPONENT_NAME}_release"
                                // and hot deploy into Jetty webapps directory
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
            }
        }

        stage('DAST') {
            agent {label "webinspect"}
            steps {
                script {
                    if (params.WI_ENABLED) {
                        sleep time: 5, unit: 'MINUTES' // wait 5 minutes for application to be ready?
                        // Run WebInspect on deployed application and upload to SSC
                        if (isUnix()) {
                            sh('"${env.WI_CLIENT_PATH}" -s "${env.WI_SETTINGS_FILE}" -macro "${env.WI_LOGIN_MACRO}" -u "${env.APP_WEBURL}" -ep "${env.WI_OUTPUT_FILE}"')
                            if (params.SSC_ENABLED) {
                                sh('"${env.SCA_CLIENT_PATH}" uploadFPR -f "${env.WI_OUTPUT_FILE}" -url "${env.SSC_WEBURL}" -authtoken "${env.SSC_AUTH_TOKEN}" -application "${env.APP_NAME}" -applicationVersion "${env.APP_VER}"')
                            }
                        } else {
                            bat(/"${env.WI_CLIENT_PATH}" -s "${env.WI_SETTINGS_FILE}" -macro "${env.WI_LOGIN_MACRO}" -u "${env.APP_WEBURL}" -ep "${env.WI_OUTPUT_FILE}"/)
                            if (params.SSC_ENABLED) {
                                bat(/"${env.SCA_CLIENT_PATH}" uploadFPR -f "${env.WI_OUTPUT_FILE}" -url "${env.SSC_WEBURL}" -authtoken "${env.SSC_AUTH_TOKEN}" -application "${env.APP_NAME}" -applicationVersion "${env.APP_VER}"/)
                            }
                        }
                    } else {
                        println "Skipping Dynamic Application Security Testing...."
                    }
                }
            }
        }
    }
}

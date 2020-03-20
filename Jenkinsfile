import groovy.json.JsonOutput

pipeline {
    agent any

    environment {
        GIT_REPO = "http://localhost:8080/gitbucket/git/mfdemo/secure-web-app.git"
        APP_NAME = "Simple Secure App"
        APP_VER = "1.0"
        JAVA_VERSION = 8
        FOD_BSI_TOKEN = credentials('jenkins-fod-bsi-token-id')
        FOD_PAT = 'FODPAT'
        FOD_USERNAME = 'kevin.lee'
        FOD_TENANT_ID = 'emeademo'
        FOD_UPLOAD_DIR = 'fod'
        COMPONENT_NAME = "secure-web-app"
        DA_USERNAME = "admin"
        DA_AUTH_TOKEN = credentials('jenkins-da-auth-token-id')
        DA_WEBURL = "http://localhost:8080/da"
        DA_CLIENT_PATH = "C:\\Micro Focus\\Deployment Automation Client\\da-client.cmd"
        DA_DEPLOY_PROCESS = "Deploy Web App"
    }

    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "M3"
    }

    stages {
        stage('Build') {
            steps {
                // Get some code from a GitHub repository
                git "${env.GIT_REPO}"

                // Get Git commit details
                script {
                    bat(/git rev-parse HEAD > .git\commit-id/)
                    //bat(/git log --format="%ae" | head -1 > .git\commit-author/)
                    env.GIT_COMMIT_ID = readFile('.git/commit-id').trim()
                    //env.GIT_COMMIT_AUTHOR = readFile('.git/commit-author').trim()
                }

                println "Git commit id: ${env.GIT_COMMIT_ID}"
                //println "Git commit author: ${env.GIT_COMMIT_AUTHOR}"

                // Run maven to build application
                bat "mvn -Dmaven.com.failure.ignore=true -Dtest=!*FailingTest clean package"
            }

            post {
                success {
                    // Record the test results
                    junit "**/target/surefire-reports/TEST-*.xml"
                    // Archive the built file
                    archiveArtifacts "target/${env.COMPONENT_NAME}.war"

                    script {
                        def useDA = fileExists 'features/da.enabled'
                        if (useDA) {
                            // upload build files into Deployment Automation component version
                            bat(/"${env.DA_CLIENT_PATH}" --weburl "${env.DA_WEBURL}" --authtoken "${env.DA_AUTH_TOKEN}" createVersion --component "${env.COMPONENT_NAME}" --name "${env.APP_VER}-${BUILD_NUMBER}"/)
                            bat(/"${env.DA_CLIENT_PATH}" --weburl "${env.DA_WEBURL}" --authtoken "${env.DA_AUTH_TOKEN}" addVersionFiles --component "${env.COMPONENT_NAME}" --version "${env.APP_VER}-${BUILD_NUMBER}" --base "${WORKSPACE}\target" --include "${env.COMPONENT_NAME}.war"/)
                            bat(/"${env.DA_CLIENT_PATH}" --weburl "${env.DA_WEBURL}" --authtoken "${env.DA_AUTH_TOKEN}" addVersionStatus --component "${env.COMPONENT_NAME}" --version "${env.APP_VER}-${BUILD_NUMBER}" --status "BUILT"/)
                        }
                    }
                }
            }

        }

        stage('Verification') {
            parallel {
                stage('SAST') {
                    steps {
                        println "Static Application Security Testing..."

                        // Get some code from a GitHub repository
                        git "${env.GIT_REPO}"

                        // Run Maven debug compile and download dependencies
                        bat "mvn -Dmaven.compiler.debuglevel=lines,vars,source -DskipTests clean verify"

                        script {
                            def useFOD = fileExists 'fortify-fod.enabled'
                            if (useFOD) {
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
                            } else {
                                // Update scan rules
                                //fortifyUpdate updateServerURL: 'https://update.fortify.com'

                                // Clean project and scan results from previous run
                                fortifyClean buildID: "${env.COMPONENT_NAME}",
                                    logFile: "${env.COMPONENT_NAME}-clean.log"

                                // Translate source files
                                fortifyTranslate buildID: "${env.COMPONENT_NAME}",
                                    projectScanType: fortifyJava(javaSrcFiles:
                                        'src\\main\\java\\com\\microfocus\\example',
                                        javaVersion: "${env.JAVA_VERSION}"),
                                    logFile: "${env.COMPONENT_NAME}-translate.log"

                                // Scan source files
                                fortifyScan buildID: "${env.COMPONENT_NAME}",
                                    resultsFile: "${env.COMPONENT_NAME}.fpr",
                                    logFile: "${env.COMPONENT_NAME}-scan.log"

                                def useSSC = fileExists 'features/ssc.enabled'
                                if (useSSC) {
                                    // Upload to SSC
                                    fortifyUpload appName: "${env.APP_NAME}",
                                        appVersion: "${env.APP_VER}",
                                        resultsFile: "${env.COMPONENT_NAME}.fpr"
                                }
                            }
                        }
                    }
                }
                stage('Deploy') {
                     steps {
                         script {
                            def useDA = fileExists 'features/da.enabled'
                            if (useDA) {
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
                                bat(/"${env.DA_CLIENT_PATH}" --weburl "${env.DA_WEBURL}" --authtoken "${env.DA_AUTH_TOKEN}" requestApplicationProcess "${WORKSPACE}\da-process.json"/)
                            }
                         }
                     }
                }
            }
        }

        stage('DAST') {
            steps {
                script {
                    def useWI = fileExists 'features/fortify-wi.enabled'
                    if (useWI) {
                        println "Dynamic Application Security Testing..."
                        // TODO: run WebInspect on deployed application
                    }
                }
            }
        }
    }
}

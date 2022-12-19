#!/usr/bin/env groovy

//****************************************************************************************************
// An example pipeline for DevSecOps using Micro Focus Fortify Application Security products
// @author: Kevin A. Lee (kevin.lee@microfocus.com)
//
//
// Pre-requisites:
// - Fortify ScanCentral SAST has been installed (for Fortify Hosted/on-premise SAST)
// - Fortify ScanCentral DAST has been installed (for Fortify Hosted/on-premise DAST)
// - Fortify Jenkins plugins has been installed and configured (if not using fcli)
// - Docker Jenkins Pipeline plugin has been installed
// - [Optional] Debricked account
// - [Optional] Sonatype Nexus IQ server has been installed for OSS SCA vulnerabilities
//
// Typical node setup:
// - Create a new Jenkins agent (or reuse one) for running Fortify Commands
// - Install Fortify ScanCentral Client on the agent machine
// - Install Fortify CLI (fcli) tool on the agent machine
// - Apply the label "fortify" to the agent.
// - Set the environment variable "FORTIFY_HOME" on the agent to point to the location of the Fortify ScanCentral Client installation
// - Set the environment variable "FORTIFY_CLI_HOME" on the agent to point to the location of the Fortify FCLI installation
//
// Credentials setup:
// Create the following redentials in Jenkins and enter values as follows:
//		iwa-git-auth-id		        - Git login as Jenkins "Username with Password" credential
//      iwa-ssc-ci-token-id         - Fortify Software Security Center "CIToken" authentication token as Jenkins Secret credential
//      iwa-edast-auth-id           - Fortify Scan Central DAST authentication as "Jenkins Username with Password" credential
//      iwa-nexus-iq-token-id       - Sonatype Nexus IQ user token as Jenkins Secret credential in form of "user code:pass code"
//      iwa-debricked-token-id      - Debricked API access token as Jenkins Secret credential
// Note: All of the credentials should be created (with empty values if necessary) even if you are not using the capabilities.
//
//****************************************************************************************************

// The instances of Docker image and container that are created
def dockerImage
def dockerContainer
def dockerContainerName = "iwa-jenkins"
def dastScanName = "iwa-jenkins"

pipeline {
    agent any

    //
    // The following parameters can be selected when the pipeline is executed manually to execute
    // different capabilities in the pipeline or configure the servers that are used.

    // Note: the pipeline needs to be executed at least once for the parameters to be available
    //
    parameters {
        booleanParam(name: 'USE_FCLI', 	    defaultValue: params.USE_FCLI ?: false,
                description: 'Use the Fortify CLI tool rather than Fortify Jenkins Plugin')
        booleanParam(name: 'SCANCENTRAL_SAST', 	defaultValue: params.SCANCENTRAL_SAST ?: false,
                description: 'Run a remote scan using Scan Central SAST (SCA) for Static Application Security Testing')
        booleanParam(name: 'SCANCENTRAL_DAST', 	defaultValue: params.SCANCENTRAL_DAST ?: false,
                description: 'Run a remote scan using Scan Central DAST (WebInspect) for Dynamic Application Security Testing')
        booleanParam(name: 'SONATYPE_SCA',      defaultValue: params.SONATYPE_SCA ?: false,
                description: 'Use Sonatype Nexus IQ for Open Source Software Composition Analysis')
        booleanParam(name: 'DEBRICKED_SCA',      defaultValue: params.DEBRICKED_SCA ?: false,
                description: 'Use Debricked for Open Source Software Composition Analysis')
        booleanParam(name: 'UPLOAD_TO_SSC',		defaultValue: params.UPLOAD_TO_SSC ?: false,
                description: 'Enable upload of scan results to Fortify Software Security Center')
        booleanParam(name: 'USE_DOCKER', defaultValue: params.USE_DOCKER ?: false,
                description: 'Package the application into a Dockerfile for running/testing')
        booleanParam(name: 'RELEASE_TO_GITHUB', defaultValue: params.RELEASE_TO_GITHUB ?: false,
                description: 'Release built and tested image to GitHub packages')
    }

    environment {
        // Application settings
        APP_NAME = "IWAPharmacyDirect"         		        // Application name
        APP_VER = "1.0-build"                               // Application version
        COMPONENT_NAME = "iwa"                              // Short form component name
        DOCKER_IMAGE_NAME = "iwapharmacydirect"             // Docker image name
        GIT_URL = scm.getUserRemoteConfigs()[0].getUrl()    // Git Repo
        JAVA_VERSION = 11                                   // Java version to compile as

        // Credential references
        GIT_CREDS = credentials('iwa-git-auth-id')
        SSC_CI_TOKEN = credentials('iwa-ssc-ci-token-id')
        NEXUS_IQ_AUTH_TOKEN = credentials('iwa-nexus-iq-token-id')
        DEBRICKED_TOKEN = credentials('iwa-debricked-token-id')

        // The following are defaulted and can be overriden by creating a "Build parameter" of the same name
        // You can update this Jenkinsfile and set defaults here for internal pipelines
        APP_URL = "${params.APP_URL ?: 'http://jenkins.onfortify.com'}" // URL of application to be tested by ScanCentral DAST
        SSC_URL = "${params.SSC_URL ?: 'http://ssc.onfortify.com'}" // URL of Fortify Software Security Center
        SSC_APP_NAME = "${params.SSC_APP_NAME ?: 'IWAPharmacyDirect'}" // Name of Application in SSC to upload results to
        SSC_APP_VERSION = "${params.SSC_APP_VERSION ?: 'build'}" // Name of Application Version in SSC to upload results to
        SSC_NOTIFY_EMAIL = "${params.SSC_NOTIFY_EMAIL ?: 'do-not-reply@microfocus.com'}" // User to notify with SSC/ScanCentral information
        SSC_SENSOR_POOL_UUID = "${params.SSC_SENSOR_POOL_UUID ?: '00000000-0000-0000-0000-000000000002'}" // UUID of Scan Central SAST Sensor Pool to use - leave for Default Pool
        SSC_SENSOR_VER = "${params.SSC_SENSOR_VER ?: '22.2'}" // ScanCentral SAST Sensor version
        SCAN_PRECISION_LEVEL = "${params.SCAN_PRECISION_LEVEL ?: 2}"  // Precision level of Fortify scan (see documentation for details)
        SCANCENTRAL_SAST_CLIENT_AUTH_TOKEN = "${params.SCANCENTRAL_SAST_CLIENT_AUTH_TOKEN ?: 'FortifyDemo'}" // ScanCentral SAST Client Authentication Token
        SCANCENTRAL_DAST_URL = "${params.SCANCENTRAL_DAST_URL ?: 'http://scancentral.onfortify.com/'}" // ScanCentral DAST API URI
        SCANCENTRAL_DAST_CICD = "${params.SCANCENTRAL_DAST_CICD ?: '56dde3cd-d15d-4d45-ab44-adedf0bc6a42'}" // ScanCentral DAST CICD identifier
        NEXUS_IQ_APP_ID = "${params.NEXUS_IQ_APP_ID ?: 'IWAPharmacyDirect'}" // Sonatype Nexus IQ App Id
        DEBRICKED_APP_ID = "${params.DEBRICKED_APP_ID ?: 'jenkins/IWAPharmacyDirect'}" // Debricked App Id
        DOCKER_OWNER = "${params.DOCKER_OWNER ?: 'fortify-presales'}" // Docker owner (in GitHub packages) to push released images to
    }

    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven 'M3'
    }

    stages {
        stage('Build') {
            agent any
            steps {

                script {
                    // Retrieve latest Git commit id
                    if (isUnix()) {
                        sh 'git rev-parse HEAD > .git/commit-id'
                    } else {
                        bat(/git rev-parse HEAD > .git\\commit-id/)
                    }
                    env.GIT_COMMIT_ID = readFile('.git/commit-id').trim()
                    echo "Git commit id: ${env.GIT_COMMIT_ID}"

                    // Run maven to build WAR/JAR application
                    if (isUnix()) {
                        sh 'mvn "-Dskip.unit.tests=false" -Dtest="*Test,!PasswordConstraintValidatorTest,!UserServiceTest,!DefaultControllerTest" -P jar -B clean verify package --file pom.xml'
                    } else {
                        bat "mvn \"-Dskip.unit.tests=false\" \"-Dtest=*Test,!PasswordConstraintValidatorTest,!UserServiceTest,!DefaultControllerTest\" -P jar -B clean verify package --file pom.xml"
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
                    script {
                        if (fileExists('target/surefire-reports')) {
                            junit "**/target/surefire-reports/TEST-*.xml"
                        }
                    }
                }
            }
        }

        stage('SAST') {
            when {
                beforeAgent true
                anyOf {
                    expression { params.SCANCENTRAL_SAST == true }
                }
            }
            // Run on an Agent with "fortify" label applied
            agent {label "fortify"}
            steps {
                script {
                    if (params.SCANCENTRAL_SAST) {

                        if (params.USE_FCLI) {
                            def uploadArg = (params.UPLOAD_TO_SSC ? '--upload' : '--noupload')
                            if (isUnix()) {
                                sh """
                                    fcli sc-sast session login --ssc-url ${env.SSC_URL} --ssc-ci-token ${SSC_CI_TOKEN} --client-auth-token "${env.SCANCENTRAL_SAST_CLIENT_AUTH_TOKEN}"
                                    scancentral package -bt mvn -bf pom.xml -sargs "-scan-precision ${env.SCAN_PRECISION_LEVEL}" -o Package.zip
                                    fcli sc-sast scan start --sensor-version ${env.SSC_SENSOR_VER} --appversion ${env.SSC_APP_NAME}:${env.SSC_APP_VERSION} -p Package.zip ${uploadArg} --store '?'
                                    fcli sc-sast scan wait-for '?' -i 5s -t 1h
                                """
                            } else {
                                bat """
                                    fcli sc-sast session login --ssc-url ${env.SSC_URL} --ssc-ci-token ${SSC_CI_TOKEN} --client-auth-token "${env.SCANCENTRAL_SAST_CLIENT_AUTH_TOKEN}"
                                    scancentral package -bt mvn -bf pom.xml -sargs "-scan-precision ${env.SCAN_PRECISION_LEVEL}" -o Package.zip
                                    fcli sc-sast scan start --sensor-version ${env.SSC_SENSOR_VER} --appversion ${env.SSC_APP_NAME}:${env.SSC_APP_VERSION} -p Package.zip ${uploadArg}  --store '?'
                                    fcli sc-sast scan wait-for '?' -i 5s -t 1h
                                """
                            }
                        } else {
                            // Set Remote Analysis options
                            def transOptions = '"-exclude \"**/Test/*.java\""'
                            def scanOptions = '"-scan-precision '.concat("${env.SCAN_PRECISION_LEVEL}").concat('"')
                            fortifyRemoteArguments transOptions: "${transOptions}", scanOptions: "${scanOptions}"

                            if (params.UPLOAD_TO_SSC) {
                                // Remote analysis (using Scan Central) with upload to SSC
                                fortifyRemoteAnalysis remoteAnalysisProjectType: fortifyMaven(buildFile: 'pom.xml', skipBuild: false),
                                        remoteOptionalConfig: [
                                                customRulepacks: "${env.WORKSPACE}" + "/etc/sca-custom-rules.xml",
                                                filterFile     : "${env.WORKSPACE}" + "/etc/sca-filter.txt",
                                                notifyEmail    : "${env.SSC_NOTIFY_EMAIL}",
                                                sensorPoolUUID : "${env.SSC_SENSOR_POOL_UUID}"
                                        ],
                                        uploadSSC: [
                                                appName   : "${env.SSC_APP_NAME}",
                                                appVersion: "${env.SSC_APP_VERSION}"
                                        ]
                            } else {
                                // Remote analysis (using Scan Central)
                                fortifyRemoteAnalysis remoteAnalysisProjectType: fortifyMaven(buildFile: 'pom.xml', skipBuild: false),
                                        remoteOptionalConfig: [
                                                customRulepacks: "${env.WORKSPACE}" + "/etc/sca-custom-rules.xml",
                                                filterFile     : "${env.WORKSPACE}" + "/etc/sca-filter.txt",
                                                notifyEmail   : "${env.SSC_NOTIFY_EMAIL}",
                                                sensorPoolUUID: "${env.SSC_SENSOR_POOL_UUID}"
                                        ]
                            }
                        }

                    } else {
                        echo "No Static Application Security Testing (SAST) to do."
                    }
                }
            }
        }

        stage('SCA OSS') {
            when {
                beforeAgent true
                anyOf {
                    expression { params.SONATYPE_SCA == true || params.DEBRICKED_SCA == true }
                }
            }
            agent any
            steps {
                script {
                    if (params.SONATYPE_SCA) {
                        nexusPolicyEvaluation advancedProperties: '',
                            enableDebugLogging: false,
                            failBuildOnNetworkError: true,
                            iqApplication: selectedApplication("${NEXUS_IQ_APP_ID}"),
                            iqModuleExcludes: [[moduleExclude: 'target/**/*test*.*']],
                            iqScanPatterns: [[scanPattern: 'target/**/*.jar']],
                            iqStage: 'build',
                            jobCredentialsId: ''
                    } else if (params.DEBRICKED_SCA) {
                        docker.image('debricked/debricked-cli').inside('--entrypoint="" -v ${WORKSPACE}:/data -w /data') {
                            sh "/home/entrypoint.sh debricked:scan '' ${env.DEBRICKED_TOKEN} ${env.DEBRICKED_APP_ID} ${env.GIT_COMMIT_ID} null cli"
                        }
                    } else {
                        echo "No Software Composition Analysis to do."
                    }

                }
            }
        }

        stage('Deploy') {
            agent any
            steps {
                script {
                    // unstash the built files
                    unstash name: "${env.COMPONENT_NAME}_release"
                    if (isUnix()) {
                        // Create docker image using JAR file
                        if (params.USE_DOCKER) {
                            dockerImage = docker.build("${env.DOCKER_OWNER}/${env.DOCKER_IMAGE_NAME}:${env.APP_VER}.${env.BUILD_NUMBER}", "-f Dockerfile .")
                        }
                    } else {
                        // Create docker image using JAR file
                        if (params.USE_DOCKER) {
                            dockerImage = docker.build("${env.DOCKER_OWNER}/${env.DOCKER_IMAGE_NAME}:${env.APP_VER}.${env.BUILD_NUMBER}", "-f Dockerfile.win .")
                        }
                    }
                }
            }
        }

        stage('DAST') {
            when {
                beforeAgent true
                anyOf {
                    expression { params.SCANCENTRAL_DAST == true }
                }
            }
            steps {
                script {
                    if (params.SCANCENTRAL_DAST && params.USE_DOCKER) {
                        // check if container is still running and if so stop/remove it
                        if (isUnix()) {
                            sh(script: "docker ps -aq --filter name=${dockerContainerName} > container.id")
                            if (fileExists('container.id')) {
                                def existingId = readFile('container.id').trim()
                                if (existingId) {
                                    echo "Found existing ${dockerContainerName} container id: ${existingId} ... deleting..."
                                    sh(script: "docker stop $existingId && docker rm -f $existingId")
                                }
                            }
                        } else {
                            bat(script: "docker ps -aq --filter name=${dockerContainerName} > container.id")
                            if (fileExists('container.id')) {
                                def existingId = readFile('container.id').trim()
                                if (existingId) {
                                    echo "Found existing ${dockerContainerName} container id: ${existingId} ... deleting..."
                                    bat(script: "docker stop ${existingId} && docker rm -f ${existingId}")
                                }
                            }
                        }

                        // start docker container
                        echo "Starting docker container ${dockerContainerName}"
                        dockerContainer = dockerImage.run("--name ${dockerContainerName} -p 9090:8080")

                        if (params.USE_FCLI) {
                            if (isUnix()) {
                                sh """
                                    fcli sc-dast session login --ssc-url ${env.SSC_URL} --ssc-ci-token ${SSC_CI_TOKEN}
                                    fcli sc-dast scan start ${dastScanName} --settings ${env.SCANCENTRAL_DAST_CICD} --start-url ${env.APP_URL} --store '?'
                                    fcli sc-dast scan wait-for '?' -i 30s -t 6h
                                """
                            } else {
                                bat """
                                    fcli sc-dast session login --ssc-url ${env.SSC_URL} --ssc-ci-token ${SSC_CI_TOKEN}}
                                    fcli sc-dast scan start ${dastScanName} --settings ${env.SCANCENTRAL_DAST_CICD} --start-url ${env.APP_URL} --store '?'
                                    fcli sc-dast scan wait-for '?' -i 30s -t 6h
                                """
                            }
                        } else {
                            // run ScanCentral DAST scan using groovy script
                            echo "Running ScanCentral DAST scan, please wait ..."
                            withCredentials([usernamePassword(credentialsId: 'iwa-edast-auth-id', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                                SCANCENTRAL_DASTApi = load 'bin/fortify-scancentral-dast.groovy'
                                SCANCENTRAL_DASTApi.setApiUri("${env.SCANCENTRAL_DAST_URL}")
                                SCANCENTRAL_DASTApi.setDebug(true)
                                SCANCENTRAL_DASTApi.authenticate("${USERNAME}", "${PASSWORD}")
                                Integer scanId = SCANCENTRAL_DASTApi.startScanAndWait("Jenkins initiated scan", "${env.SCANCENTRAL_DAST_CICD}", 5)
                                String scanStatus = SCANCENTRAL_DASTApi.getScanStatusValue(SCANCENTRAL_DASTApi.getScanStatusId(scanId))
                                echo "ScanCentral DAST scan id: ${scanId} - status: ${scanStatus}"
                            }
                        }

                    } else {
                        echo "No Dynamic Application Security Testing (DAST) to do."
                    }
                }
            }
        }

        // An example release gate/checkpoint
        stage('Gate') {
            agent any
            steps {
                script {
                    if (params.USE_FCLI) {
                        if (isUnix()) {
                            sh """
                                fcli ssc session login --url ${env.SSC_URL} --ci-token ${SSC_CI_TOKEN}
                                fcli ssc appversion-vuln count --appversion ${env.SSC_APP_NAME}:${env.SSC_APP_VERSION}
                            """
                        } else {
                            bat """
                                fcli sc-dast session login --url ${env.SSC_URL} --ci-token ${SSC_CI_TOKEN}}
                                fcli ssc appversion-vuln count --appversion ${env.SSC_APP_NAME}:${env.SSC_APP_VERSION}
                            """
                        }
                    } else {
                        input id: 'Release',
                                message: 'Ready to Release?',
                                ok: 'Yes, let\'s go',
                                submitter: 'admin',
                                submitterParameter: 'approver'
                    }
                }
            }
        }

        stage('Release') {
            agent any
            steps {
                script {
                    // Example publish to GitHub packages
                    if (params.RELEASE_TO_GITHUB) {
                        docker.withRegistry('https://ghcr.io', 'iwa-git-auth-id') {
                            dockerImage.push("${env.APP_VER}.${BUILD_NUMBER}")
                            // and tag as "latest"
                            dockerImage.push("latest")
                        }
                    } else {
                        echo "No releasing to do."
                    }
                }
            }
        }

    }

    post {
        always {
            script {
                // check if container is still running and if so stop/remove it
                if (params.USE_DOCKER) {
                    if (isUnix()) {
                        sh(script: "docker ps -aq --filter name=${dockerContainerName} > container.id")
                        if (fileExists('container.id')) {
                            def existingId = readFile('container.id').trim()
                            if (existingId) {
                                echo "Found existing ${dockerContainerName} container id: ${existingId} ... deleting..."
                                sh(script: "docker stop $existingId && docker rm -f $existingId")
                            }
                        }
                    } else {
                        bat(script: "docker ps -aq --filter name=${dockerContainerName} > container.id")
                        if (fileExists('container.id')) {
                            def existingId = readFile('container.id').trim()
                            if (existingId) {
                                echo "Found existing ${dockerContainerName} container id: ${existingId} ... deleting..."
                                bat(script: "docker stop ${existingId} && docker rm -f ${existingId}")
                            }
                        }
                    }
                }
            }
        }
    }

}

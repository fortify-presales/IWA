![Continuous Inspection](https://github.com/mfdemo/IWA/workflows/Continuous%20Inspection/badge.svg)

# IWA (Insecure Web App) Pharmacy Direct

#### Table of Contents

*   [Overview](#overview)
*   [Forking the Repository](#forking-the-repository)
*   [Building the Application](#building-the-application)
*   [Running the Application](#running-the-application)
*   [Application Security Testing Integrations](#application-security-testing-integrations)
    * [SAST using Fortify SCA command line](#static-analysis-using-fortify-sca-command-line)
    * [SAST using Fortify SCA maven plugin](#static-analysis-using-fortify-sca-maven-plugin)
    * [SAST using Fortify ScanCentral SAST](#static-analysis-using-fortify-scancentral-sast)
    * [Open Source Susceptibility Analysis using Sonatype Nexus IQ](#open-source-susceptibility-analysis-using-sonatype-nexus-iq)
    * [SAST using Fortify on Demand](#static-analysis-using-fortify-on-demand)
    * [DAST using Fortify WebInspect](#dynamic-analysis-using-fortify-webinspect)
    * [DAST using Fortify ScanCentral DAST](#dynamic-analysis-using-fortify-scancentral-dast)
    * [API Security Testing using Fortify WebInspect and Postman](#api-security-testing-using-fortify-webinspect-and-postman)
    * [DAST using Fortify on Demand](#dynamic-analysis-using-fortify-on-demand)
*   [Build and Pipeline Integrations](#build-and-pipeline-integrations)
    * [Jenkins Pipeline](#jenkins-pipeline)
    * [GitHub Actions](#github-actions)
    * [Azure DevOps Pipeline](#azure-devops-pipelines)
    * [GitLab CI/CD](#gitlab-cicd)
*   [Developing and Contributing](#developing-and-contributing)
*   [Licensing](#licensing)

## Overview

_IWA (Insecure Web App) Pharmacy Direct_ is an example Java/Spring Web Application for use in **DevSecOps** scenarios and demonstrations.
The source code includes some examples of insecure code - which can be found using static and dynamic application
security testing tools such as [Fortify SCA](https://www.microfocus.com/en-us/products/static-code-analysis-sas),
[Fortify On Demand](https://www.microfocus.com/en-us/products/application-security-testing)
and [Fortify WebInspect](https://www.microfocus.com/en-us/products/webinspect-dynamic-analysis-dast).

One of the main aims of this project is to illustrate how security can be embedded early and continuously in
the development lifecycle - so a number of "integrations" to common build and pipeline tools are provided.

The application is intended to provide the functionality of a typical "online pharmacy", including purchasing Products (medication)
and requesting Services (prescriptions, health checks etc) - however it is mostly a work in progress!

*Please note: the application should not be used in a production environment!*

![Screenshot](media/screenshot.png)

## Forking the Repository

> In order to execute the example scenarios described here you will need to "fork" a copy of this repository into
your own GitHub account. The process of "forking" is described in detail in the [GitHub documentation](https://docs.github.com/en/github/getting-started-with-github/fork-a-repo) - you can
start the process by clicking on the "Fork" button at the top right.

## Building the Application

To build (and unit test) the application, execute the following from the command line:

```
mvn clean package
```

This will create a JAR file (called `iwa.jar`) in the `target` directory.

To build a WAR file for deployment to an application server such as [Apache Tomcat](http://tomcat.apache.org/)
execute the following:

```
mvn -Pwar clean package
```

This will create a WAR file (called `iwa.war`) in the `target` directory.

## Running the Application

There are a number of ways of running the application depending on the scenario(s) that
you wish to execute.

### Development (IDE/command line)

To run (and test) locally in development mode, execute the following from the command line:

```
mvn spring-boot:run
```

Then navigate to the URL: [http://localhost:8080](http://localhost:8080). 

The website allows you to login with the following default users:

- Standard user: **user/password**
- Administration user: **admin/password**

### Release (Docker Image)

The JAR file can be built into a [Docker](https://www.docker.com/) image using the provided `Dockerfile` and the
following commands:

```
mvn -Pjar clean package
docker build -t iwa .
```

or on Windows:

```
mvn -Pjar clean package
docker build -f Dockerfile.win -t iwa .
```

This image can then be executed using the following commands:

```
docker run -d -p 8080:8080 iwa
```

There is also an example `docker-compose.yml` file that illustrates how to run the application with HTTPS/SSL using
[nginx](https://www.nginx.com/) and [certbot](https://certbot.eff.org/) - please note this is for reference only as it uses a specific domain name.

## Application Security Testing Integrations

### SAST using Fortify SCA command line

There is an example PowerShell script [fortify-sca.ps1](bin/fortify-sca.ps1) that you can use to execute static application security testing
via [Fortify SCA](https://www.microfocus.com/en-us/products/static-code-analysis-sast/overview). This script runs a
"sourceanalyzer" translation and scan on the project's source code. It creates a Fortify Project Results file called `target\iwa.fpr`
which you can open using the Fortify `auditworkbench` tool. It also creates a PDF report called `iwa.pdf` and optionally
uploads the results to [Fortify Software Security Center](https://www.microfocus.com/en-us/products/software-security-assurance-sdlc/overview).

### SAST using Fortify SCA Maven plugin

To carry out a Fortify SCA scan with Maven you first need to have installed the Maven plugin. This plugin
is found in the `_FORTIFY_SCA_HOME_\plugins\maven` directory. Once you have installed the
plugin you can then execute the commands similar to the following:

```
mvn com.fortify.sca.plugins.maven:sca-maven-plugin:20.2.0:clean
mvn package -DskipTests com.fortify.sca.plugins.maven:sca-maven-plugin:20.2.0:translate
mvn com.fortify.sca.plugins.maven:sca-maven-plugin:20.2.0:scan -Dfortify.sca.Xmx=3G
```

This will product an FPR file in the `target\fortify` directory. You can optionally produce a
PDF report using the `ReportGenerator` tool with the following command:

```
ReportGenerator -Dcom.fortify.sca.ProjectRoot=target\fortify -user "Demo User" -format pdf -f target\fortify\iwa.pdf `
    -source target\fortify\iwa.fpr
```

### SAST using Fortify ScanCentral SAST

The provided [Jenkinsfile](Jenkinsfile) includes an example for running a remote scan using Fortify ScanCentral SAST. There is also a PowerShell
script [fortify-scancentral-sast.ps1](bin\fortify-scancentral-sast.ps1) for running a remote scan from the command
line.

### Open Source Susceptibility Analysis using Sonatype Nexus IQ

The provided [Jenkinsfile](Jenkinsfile) includes support for running open source susceptibility analysis using the integration between
[Sonatype Nexus IQ](https://www.sonatype.com/nexus/lifecycle) and the [Fortify Source And Lib Scanner](https://marketplace.microfocus.com/fortify/content/fortify-sourceandlibscanner).
There is also an example PowerShell script file [fortify-sourceandlibscanner.ps1](bin\fortify-sourceandlibscanner.ps1)
for running a scan from the command line.

### SAST using Fortify on Demand

To execute a [Fortify on Demand](https://www.microfocus.com/en-us/products/application-security-testing/overview) SAST scan
you need to package and upload the source code to Fortify on Demand. To prepare the source code to be uploaded you can
execute the following command:

```
mvn -Dmaven.compiler.debuglevel=lines,vars,source -DskipTests -Pfortify clean verify package
```

This will create a directory called `fod` which you can *Zip* up and upload to Fortify on Demand.

There is also an example PowerShell script file [fortify-fod.ps1](bin/fortify-fod.ps1) that you can run to start a Fortify on Demand static scan.
It can be invoked via the following from a PowerShell prompt:

```PowerShell
# Create the fod.zip Zip file
Compress-Archive -Path .\fod -DestinationPath .\fod.zip
# Upload and start the static scan
.\bin\fortify-fod.ps1 -ZipFile '.\fod.zip' -ApplicationName 'IWA' -ReleaseName 'master' -Notes 'GitHub Action initiated scan' `
    -FodApiUri 'https://api.emea.fortify.com' -FodApiUsername 'FOD_ACCESS_KEY' -FodApiPassword 'FOD_SECRET_KEY'
``` 

where `FOD_ACCESS_KEY` and `FOD_SECRET_KEY` are the values of an API Key and Secret you have created in the Fortify on
Demand portal.

### DAST using Fortify WebInspect

To carry out a WebInspect scan you should first deploy the application to a Java Application server such as [Apache Tomcat](https://tomcat.apache.org/)
or start it using the Docker approach described above. Then you can start a scan using the following:

```
"C:\Program Files\Fortify\Fortify WebInspect\WI.exe\WI.exe" -s ".\etc\IWA-UI-Dev-Settings.xml" -macro ".\etc\IWA-UI-Dev-Login.webmacro" -u "http://localhost:8080" -ep ".\target\wi-iwa.fpr" -ps 1008
```

This will start a scan using the Default Settings and Login Macro files provided in the `etc` directory. It assumes
the application is running on "localhost:8080". It will run a "Critical and High Priority" scan using the policy with id 1008. 
Once completed you can open the WebInspect "Desktop Client" and navigate to the scan created for this execution.

An FPR called `wi-iwa.fpr` will be available in the `target` directory. You can generate a
PDF report from this file using `ReportGenerator` or upload it to Fortify SSC or Fortify on Demand.

There is an example PowerShell script file [fortify-webinspect.ps1](bin\fortify-webinspect.ps1) that you can run to 
execute the above commands.

### DAST using Fortify ScanCentral DAST

The provided [Jenkinsfile](Jenkinsfile) includes support for running a remote scan using Fortify ScanCentral DAST through a Groovy
script [fortify-scancentral-dast.groovy](bin\fortify-scancentral-dast.groovy). 

There is also an example PowerShell script file [fortify-scancentral-dast.ps1](bin\fortify-scancentral-dast.ps1).
It can be invoked via the following from a PowerShell prompt:

```PowerShell
.\bin\fortify-scancentral-dast.ps1 -ApiUri 'SCANCENTRAL_DAST_API' -Username 'SSC_USERNAME' -Password 'SSC_PASSWORD' `
    -CiCdToken 'CICD_TOKEN_ID'
``` 

where `SCANCENTRAL_DAST_API` is the URL of the ScanCentral DAS API configured in Software Security Center and
`SSC_USERNAME` and `SSC_PASSWORD` are the login credentials of a Software Security Center user who is permitted to
run scans. Finally, `CICD_TOKEN_ID` is the CICD identifier of a "Scan Settings" you have previously created.

### API Security Testing using Fortify WebInspect and Postman

The IWA application includes a fully documented [Swagger](https://swagger.io/solutions/getting-started-with-oas/) based 
API which you can browse to at 
[http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config](http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config).
You can carry out security testing of this API using Fortify WebInspect or ScanCentral DAST. A [Postman](https://www.postman.com/downloads/) 
collection is provided to help in this. You can exercise the collection using [newman](https://github.com/postmanlabs/newman) 
from the command line as follows:

```PowerShell
newman run .\etc\IWA-API.postman_collection.json --environment .\etc\IWA-API-Dev.postman_environment.json
```

You can also use this collection with WebInspect as follows: 

```PowerShell
"C:\Program Files\Fortify\Fortify WebInspect\WI.exe\WI.exe" -pwc ".\etc\IWA-API-Dev.postman_collection.json" -ps 1008
```

### DAST using Fortify on Demand

You can invoke a Fortify on Demand dynamic scan using the [PowerShellForFOD](https://github.com/fortify-community-plugins/PowerShellForFOD) PowerShell module that i have written.
For examples on how to achieve this see [here](https://github.com/fortify-community-plugins/PowerShellForFOD/blob/master/USAGE.md#starting-a-dynamic-scan).

## Build and Pipeline Integrations

### Jenkins

If you are using [Jenkins](https://jenkins.io/), a comprehensive `Jenkinsfile` is provided to automate all of the typical
steps of a DevSecOps Continuous Delivery (CD) process. For application security testing it can make use of
[Fortify SCA](https://www.microfocus.com/en-us/products/static-code-analysis-sast/),
[Fortify WebInspect](https://www.microfocus.com/en-us/products/webinspect-dynamic-analysis-dast/),
[Fortify SSC](https://www.microfocus.com/en-us/products/software-security-assurance-sdlc/)
and/or [Fortify on Demand](https://www.microfocus.com/en-us/products/application-security-testing/).

To make use of the `Jenkinsfile` create a new Jenkins *Pipeline* Job and in the *Pipeline*
section select `Pipeline script from SCM` and enter the details of a forked version of this GitHub repository.

The first run of the pipeline should be treated as a "setup" step as it will
create some *Job Parameters* which you can then select to determine which features
you want to enable in the pipeline.

You will need to have installed and configured the [Fortify SCA](https://plugins.jenkins.io/fortify/)
and/or the [Fortify on Demand Uploader](https://plugins.jenkins.io/fortify-on-demand-uploader/) Jenkins plugins.

There is lots of documentation in the `Jenkinsfile` itself so please examine it to see what else
you will need to do for a successful invocation.

### GitHub Actions

This repository includes a [GitHub Actions](https://github.com/features/actions) example
[workflow](.github/workflows/continuous_inspection.yml) that
automates the build of the application and uploads the source code to
[Fortify on Demand](https://www.microfocus.com/en-us/products/application-security-testing) for SAST.

The example workflow runs on every push to the *master* branch and on every "pull request" that is created.

It makes use of the [Fortify GitHub Actions](https://github.com/marketplace/actions/fortify-on-demand-scan).

There are also additional user-initiated workflows than can be run ad-hoc for executing individual static or
dynamic scans.

### GitLab CI/CD

This repository includes a [GitLab Pipelines](https://docs.gitlab.com/ee/ci/pipelines/) example
[pipeline](.gitlab-ci.yml) that automates the build of the application and uploads the source code to
[Fortify on Demand](https://www.microfocus.com/en-us/products/application-security-testing) for SAST.

It makes use of the [Fortify GitLab CI Templates](https://gitlab.com/Fortify/gitlab-ci-templates)

### Azure DevOps Pipelines

TBD

## Developing and Contributing

Please see the [Contribution Guide](CONTRIBUTING.md) for information on how to develop and contribute.

If you have any problems, please consult [GitHub Issues](https://github.com/mfdemo/iwa/issues) to see if has already been discussed.

Ring2Park Web
=============

_Ring2Park Web_ is an example Java/Spring Web Application for use in DevSecOps scenarios and demonstrations.
The source code includes some insecure coding examples for use in static and dynamic application security 
testing scenarios and so should not be used in a production environment.

Building the application
------------------------ 

To build (and unit test) the application execute the following from the command line:

```
mvn clean package
```

This will create a JAR file (called `ring2park-web.jar`) in the `target` directory.

To build a WAR file for deployment to an application server such as [Apache Tomcat](http://tomcat.apache.org/) 
execute the following:

```
mvn -Pwar clean package
```

This will create a WAR file (called `ring2park-web.war`) in the `target` directory.

Running the application
-----------------------

There are a number of ways of running the application depending on the scenario(s) that
you wish to execute. 

To run (and test) locally in development mode execute the following from the command line:

```
mvn spring-boot:run
```

Then navigate to the URL: [http://localhost:9080/](http://localhost:9080/). The web site
allows you to login with the following default users:

 - Standard user: **user/password**
 - Administration user: **admin/password**

You can also deploy the application to [WebSphere Liberty](https://developer.ibm.com/wasdev/websphere-liberty/)
using the following commands:

```
mvn -Pwar,wlp clean package liberty:create liberty:install-feature liberty:deploy liberty:start
```

This will download a copy of WebSphere Liberty from the Internet, create a new server and
deploy the application into it. You can then navigate to either of the following URLS:

 - [http://localhost:6060/ring2park-web](http://localhost:6060/ring2park-web)
 - [https://localhost:6643/ring2park-web](https://localhost:6643/ring2park-web)

You can stop the WebSphere Liberty server using the following command:

```
mvn -Pwlp liberty:stop
```

Finally, the JAR file can be built into a [Docker](https://www.docker.com/) image using the 
provided `Dockerfile` and the following commands:

```
mvn -Pjar package
docker build -t ring2park-app .
```

The `Dockerfile` provided is for creating Linux image (recommended). There is also a
`Dockerfile.win` for creating a Windows image if required. 

Using GitHub Actions
--------------------

This repository includes a [GitHub Actions](https://github.com/features/actions) example
[workflow](.github/workflows/continuous_inspection.yml) that
automates the build of the application and uploads the source code to
[Fortify on Demand](https://www.microfocus.com/en-us/products/application-security-testing) for static analysis. 

To integrate with Fortify on Demand it makes use of [fod-github-action](https://github.com/fortify-community-plugins/fod-github-action).
The example workflow runs on every push to the *master* branch and on every "pull request" that is created.

Using Jenkins
-------------

If you are using [Jenkins](https://jenkins.io/), a comprehensive `Jenkinsfile` 
is provided to automate all of the typical steps of DevSecOps Continuous Delivery (CD)
process. For application security testing it can make use of [Fortify SCA](https://www.microfocus.com/en-us/products/static-code-analysis-sast/), 
[Fortify WebInspect](https://www.microfocus.com/en-us/products/webinspect-dynamic-analysis-dast/), 
[Fortify SSC](https://www.microfocus.com/en-us/products/software-security-assurance-sdlc/) 
and/or [Fortify on Demand](https://www.microfocus.com/en-us/products/application-security-testing/). 

To make use of the `Jenkinsfile` create a new Jenkins *Pipeline* Job and in the *Pipeline* 
section select `Pipeline script from SCM`. Enter the details for this GitHub repository.

The first run of the pipeline should be treated as a "setup" step as it will
create some *Job Parameters* which you can then select to determine which features
you want to enable in the pipeline.

You will need to have installed and configured the [Fortify SCA](https://plugins.jenkins.io/fortify/) 
and/or the [Fortify on Demand Uploader](https://plugins.jenkins.io/fortify-on-demand-uploader/) plugins.

There is also an optional capability to deploy the application (to different environments) and run security
testing using [Micro Focus Deployment Automation](https://www.microfocus.com/en-us/products/deployment-automation/overview).

There is lots of documentation in the `Jenkinsfile` itself so please examine it to see what else
you will need to create for a successful invocation.

Carrying out a Fortify SCA scan (via sourceanalyzer)
----------------------------------------------------

There is an example Windows `.bat` file that you can use to run `sourceanalyzer` and produce
a PDF report. You can execute it using the following command:

```
fortify-sca.bat
```

This will produce a PDF report called `ring2park-web.pdf` in the root directory.

Carrying out a Fortify SCA scan (via Maven plugin)
--------------------------------------------------

To carry out a Fortify SCA scan you first need to have installed the Maven plugin. This plugin
is found in the  `_FORTIFY_SCA_HOME_\plugins\maven` directory. Once you have installed the 
plugin you can then execute the following commands:

```
mvn com.fortify.sca.plugins.maven:sca-maven-plugin:20.1.0:clean
mvn package -DskipTests com.fortify.sca.plugins.maven:sca-maven-plugin:20.1.0:translate
mvn com.fortify.sca.plugins.maven:sca-maven-plugin:20.1.0:scan -Dfortify.sca.Xmx=3G
```

This will product an FPR file in the `target\fortify` directory. You can optionally produce a
PDF report using the `ReportGenerator` tool with the following command:

```
ReportGenerator -Dcom.fortify.sca.ProjectRoot=target\fortify -user "Demo User" -format pdf -f target\fortify\ring2park-web.pdf -source target\fortify\ring2park-web-1.0-SNAPSHOT.fpr
```

or use the _fortify-sca.bat_ script from the command line.

Carrying out a Fortify WebInspect scan (from command line)
----------------------------------------------------------

To carry out a WebInspect scan you should first deploy the application to a Java Application server such as [Apache Tomcat](https://tomcat.apache.org/).
However, as this project has been already pre-configured to download, configure and deploy to a copy of [WebSphere Liberty](https://www.ibm.com/cloud/websphere-liberty)
you can use the following:

```
l
```

Then you can start a scan using the following:

```
"WEBINSPECT_INSTALL_DIR\WI.exe" -s ".\etc\DefaultSettings.xml" -macro ".\etc\Login.webmacro" -u "-i/" -ep ".\target\wi-ring2park-web.fpr" -ps 1008
```

This will start a scan using the Default Settings and Login Macro files provided in the `etc` directory. 
It will run a "Critical and High Priority" scan using the policy with id 1008. Once completed you can
open the WebInspect "Desktop Client" and navigate to the scan created for this execution.

An FPR called `wi-ring2park-web.fpr` will be available in the `target` directory. You can generate a 
PDF report from this file using `ReportGenerator` or upload it to Fortify SSC or Fortify on Demand.

There is an example Windows `.bat` file that you can use to run `WI.exe` and produce a PDF report. 
You can execute it using the following command:

```
fortify-wi.bat
```

Once you have finished testing the application with WebInspect you can stop WebSphere Liberty using the following:
```
mvn -Pwlp liberty:stop
```

Carrying out a Fortify on Demand scan (from command line)
----------------------------------------------------------

To execute a Fortify on Demand scan you need to upload the source code to scan. To prepare the source
code to be uploaded you can execute the following command:

```
mvn -Dmaven.compiler.debuglevel=lines,vars,source -DskipTests -P fortify clean package
```

This will create a directory called `fod` which you can *Zip* and upload using [fod-uploader-java](https://github.com/fod-dev/fod-uploader-java/)
with a command similar to the following:

```
java -jar _PATH_TO_DIR_/FodUpload.jar -bsi <token> -z fod.zip -uc <username> <password> -ep 1 -rp 2 -pp 0 -a 1
```

The exact command will depend on your own setup, so please check the documentation first.

---

If you have any problems, please consult [GitHub Issues](https://github.com/mfdemo/ring2park-web/issues) to see if has already been discussed.

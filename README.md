Secure Web App
==============

A Java/Spring Boot Web Application for use in [Micro Focus Fortify](https://www.microfocus.com/en-us/solutions/application-security) demonstrations.

To run (and test) locally execute the following from the command line:

```
mvn spring-boot:run
```

Then navigate to the default URL: http://localhost:9080/. The following default users are supplied:

 - Standard user: **user/password**
 - Administration user: **admin/password**

To build execute the following from the command line:

```
mvn clean package
``` 

This will create a JAR file in the _target_ directory which can executed directly using the following:

```
java -Dserver.port=9080 -jar target/secure-web-app.jar
```
The JAR file can be built into a Docker image using the provided _Dockerfile_ and the following:

```
docker build -t secure-web-app .
``` 

Alternately, you can create a WAR file which can de deployed to a Java Application Server such as Jetty or 
Apache Tomcat, using the following:

```
mvn -P war clean package
```

To carry out Fortify SCA scan:

```
mvn com.fortify.sca.plugins.maven:sca-maven-plugin:19.2.0:clean
mvn package -DskipTests com.fortify.sca.plugins.maven:sca-maven-plugin:19.2.0:translate
mvn com.fortify.sca.plugins.maven:sca-maven-plugin:19.2.0:scan -Dfortify.sca.Xmx=3G
```

or use the _fortify-sca.bat_ script from the command line.

If using [Jenkins](https://jenkins.io/), there is a fully configured _Jenkinsfile_ to automate a build pipeline.


Fortify Annotations
-------------------

Installed with:

```
mvn install:install-file -Dfile="C:\Micro Focus\Fortify_SCA_and_Apps_19.2.0\Samples\advanced\javaAnnotations\libraries\FortifyAnnotations-SOURCE.jar" -DcreateChecksum -DupdateReleaseInfo=true -DgroupId=com.microfocus.fortify -DartifactId=FortifyAnnotations -Dclassifier=SOURCE -Dversion=19.2.0 -Dpackaging=jar -DlocalRepositoryPath=.\lib
```

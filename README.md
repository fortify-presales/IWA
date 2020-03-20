Secure Web App
==============

A very simple Java/Spring Boot Web Application for use in demonstrations.

To run (and test) locally execute the following from the command line:

```mvnw spring-boot:run```

To build execute the following from the command line:

```mvnw clean package``` 

This will create a JAR and WAR file in the target directory which can be deployed to container 
or an Application Server such as Apache Tomcat.

To carry out Fortify SCA scan:

```
mvn com.fortify.sca.plugins.maven:sca-maven-plugin:19.2.0:clean
mvn package -DskipTests com.fortify.sca.plugins.maven:sca-maven-plugin:19.2.0:translate
mvn com.fortify.sca.plugins.maven:sca-maven-plugin:19.2.0:scan -Dfortify.sca.Xmx=3G
```

or use the _fortify-sca.bat__ script from the command line.

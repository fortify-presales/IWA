FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=secure-web-app.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","${JAVA_OPTS}","-jar","/app.jar"]

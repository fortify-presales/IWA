FROM eclipse-temurin:11-jdk-alpine

LABEL maintainer="klee2@opentext.com"

RUN apk add --update bash bash-completion curl iputils-ping

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8088 available to the world outside this container
EXPOSE 8088

# The application's jar file
ARG JAR_FILE=build/libs/FortifyDemoApp.jar

# Copy the application's jar to the container
COPY ${JAR_FILE} app.jar

# JAVA_OPTS to be passed in
ENV JAVA_OPTS="-Xmx512m -Xss256k"

# Run the jar file
ENTRYPOINT ["java","-jar","/app.jar"]

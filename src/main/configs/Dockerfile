FROM openjdk:11-jdk-slim

LABEL maintainer="kevin.lee@microfocus.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8080

# The application's jar file
ARG JAR_FILE=target/iwa.jar

# Copy the application's jar to the container
COPY ${JAR_FILE} app.jar

# JAVA_OPTS to be passed in
ENV JAVA_OPTS="-Xmx512m -Xss256k"

# Run the jar file
ENTRYPOINT ["java","-jar","/app.jar"]

FROM openjdk:8-jdk-alpine

LABEL maintainer="kevin.lee@microfocus.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8080

# The application's jar file
ARG JAR_FILE=target/secure-web-app.jar

# Copy the application's jar to the container
COPY ${JAR_FILE} app.jar

# Allow JAVA_OPTS to be passed in
ENV JAVA_OPTS=""

# Run the jar file
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar"]

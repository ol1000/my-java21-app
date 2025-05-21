 # Use an official OpenJDK 21 runtime as a parent image
 FROM openjdk:21-jdk-slim

 # Set the working directory in the container
 WORKDIR /app

 # Download the New Relic Java agent
 ARG NEW_RELIC_VERSION=latest
 RUN curl -O https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic-java.zip && \
     unzip newrelic-java.zip && \
     mv newrelic/newrelic.jar . && \
     rm -rf newrelic newrelic.jar.zip

 # Copy the Maven wrapper
 COPY mvnw .
 COPY .mvn ./.mvn

 # Copy the pom.xml file
 COPY pom.xml .

 # Download dependencies (this layer will be cached if pom.xml doesn't change)
 RUN ./mvnw dependency:go-offline -B

 # Copy the application source code
 COPY src ./src

 # Package the application
 RUN ./mvnw package -DskipTests

 # Define environment variable for Spring Boot profile (optional)
 ENV SPRING_PROFILES_ACTIVE=docker

 # Specify the application's jar file
 ARG JAR_FILE=target/*.jar

 # Expose the port the application runs on
 EXPOSE 8080

 # Set New Relic environment variables
 ENV NEW_RELIC_APP_NAME="YourJava21App-Docker"
 ENV NEW_RELIC_LICENSE_KEY="1c0ecbca7554fedde4f50efd6ddc8b11FFFFNRAL"

 # Define the command to run the application with the New Relic agent
 ENTRYPOINT ["java", "-javaagent:./newrelic.jar", "-jar", JAR_FILE]

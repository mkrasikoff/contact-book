# Use an official Tomcat runtime as a parent image
FROM tomcat:9.0.48-jdk11-openjdk-slim

# Set the working directory in the container to Tomcat webapps directory
WORKDIR /usr/local/tomcat/webapps/

# Copy the built WAR file from your target directory to the Tomcat webapps directory
COPY /target/spring-mvc-app.war ./

# Make port 8080 available
EXPOSE 8080

# Run the catalina.sh run command to start Tomcat
CMD ["catalina.sh", "run"]
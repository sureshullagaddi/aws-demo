# Use a base image with OpenJDK 17 (or other suitable version)
FROM openjdk:17-jdk-slim AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file into the container
COPY build/libs/aws-demo-0.0.1-SNAPSHOT.jar /app/aws-demo.jar

# Expose the port your Spring Boot app will run on
EXPOSE 8080

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "aws-demo.jar"]

#commands to run locally after installation of docker in local
#1.docker build -t aws-demo:latest .
#2.docker images
#3.docker run -p 8080:8080 aws-demo:latest

# push docker image to docker hub
#1.create docker repo
#2.login to local terminal - docker login give user name and password
#3.docker tag aws-demo:latest 7829099824/aws-repo:latest
#4.docker push 7829099824/aws-repo:latest

#find the way to push code to github - build image  - push to aws ec2 - push 1 copy to dockerhub - automate - deploy using kubernates
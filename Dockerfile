# Use Maven base image to build the project
FROM maven:3.9.3-amazoncorretto-20 AS build

# Set the working directory in Docker
WORKDIR /usr/src/app

# Copy the pom.xml and src directory (Your source code) to the image
COPY pom.xml .
COPY src ./src

# Package the application into a fat JAR
RUN mvn clean package -DskipTests

# Now build the runtime image
FROM openjdk:20-slim

# Install necessary packages and tools
RUN apt-get update && \
    apt-get install -y wget net-tools && \
    rm -rf /var/lib/apt/lists/*

# Download and extract Kafka tarball
RUN wget https://downloads.apache.org/kafka/3.5.1/kafka_2.13-3.5.1.tgz && \
    tar -xzf kafka_2.13-3.5.1.tgz && \
    rm kafka_2.13-3.5.1.tgz

# Copy the built JAR from the build stage into this image
COPY --from=build /usr/src/app/target/ktable-app.jar /usr/app/ktable-app.jar



ENTRYPOINT ["java","-jar","/usr/app/ktable-app.jar"]

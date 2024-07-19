FROM openjdk:17-jdk-alpine

# Add Maintainer Info
LABEL maintainer="Leni <lenicoulibaly@gmail.com>"
ENV PORT=5000

# Make port 8080 available to the world outside this container
EXPOSE ${PORT}

# The application's jar file
ARG JAR_FILE=synchronRE.jar

# Add the application's jar to the container
ADD ${JAR_FILE} synchronRE.jar
ENV SPRING_CONFIG_LOCATION="./config/"

# Run the jar file
ENTRYPOINT ["java", "-jar", "-Djava.security.egd=file:/dev/./urandom", "/synchronRE.jar", "--spring.config.location=${SPRING_CONFIG_LOCATION}"]
#docker run -v C:\workspace\artefacts\sychronre\config:/config synchronre -p 5000:5000
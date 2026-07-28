#FROM eclipse-temurin:21-jre

#WORKDIR /app

#COPY target/*.jar app.jar

#EXPOSE 8080

#ENTRYPOINT ["java", "-jar", "app.jar"]

#$env:DB_URL="jdbc:mysql://localhost:3306/fitness_data"
#$env:DB_USER="root"
#$env:DB_PWD="root"
#run './mvnw package' for creating jar file in target directory
#must run all these commands first before running 'docker build -t fitness-monolith .'

#for creating container from image run
# 'docker run -p 8080:8080 -e DB_URL=jdbc:mysql://host.docker.internal:3306/fitness_data -e DB_USER=root -e DB_PWD=root fitness-monolith'


FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests


FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
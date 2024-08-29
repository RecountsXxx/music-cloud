# First stage: build the application
FROM eclipse-temurin:21-jdk as build
WORKDIR /workspace/app

# Copy and cache dependencies
COPY ./src/pom.xml .
COPY ./src/mvnw .
COPY ./src/.mvn .mvn
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

# Copy source code and build the application
COPY ./src/src src
RUN ./mvnw package -DskipTests
RUN ./mvnw clean install -DskipTests

# Second stage: create the runtime image
FROM eclipse-temurin:21-jre
VOLUME /tmp
WORKDIR /app
COPY --from=build /workspace/app/target/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]
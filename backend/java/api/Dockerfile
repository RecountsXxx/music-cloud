#FROM eclipse-temurin:22-jdk as build
#WORKDIR /workspace/app
#
#COPY ./src/mvnw .
#COPY ./src/.mvn .mvn
#COPY ./src/pom.xml .
#COPY ./src/src src
#
#RUN ./mvnw install -DskipTests
#RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)
#
#FROM eclipse-temurin:22-jdk
#VOLUME /tmp
#ARG DEPENDENCY=/workspace/app/target/dependency
#COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
#COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
#COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
#ENTRYPOINT ["java","-cp","app:app/lib/*","org.deus.src.SrcApplication"]

# First stage: build the application
FROM eclipse-temurin:22-jdk as build
WORKDIR /workspace/app

# Copy and cache dependencies
COPY ./src/pom.xml .
COPY ./src/mvnw .
COPY ./src/.mvn .mvn

RUN sed -i 's/\r$//' mvnw
#RUN ./mvnw dependency:go-offline

# Copy source code and build the application
COPY ./src/src src
RUN ./mvnw package -DskipTests

# Second stage: create the runtime image
FROM eclipse-temurin:22-jre
VOLUME /tmp
WORKDIR /app
COPY --from=build /workspace/app/target/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]
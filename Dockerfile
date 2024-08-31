FROM gradle:jdk21 AS build
WORKDIR /notesapi
COPY . .
RUN chmod +x gradlew
RUN ./gradlew clean bootJar

FROM eclipse-temurin:21-jdk
WORKDIR /notesapi
COPY --from=build notesapi/build/libs/notes-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "app.jar" ]
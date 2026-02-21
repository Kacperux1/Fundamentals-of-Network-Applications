FROM maven:3.9-eclipse-temurin-21 as build_stage

COPY ./pom.xml ./
COPY ./src ./src

RUN mvn package -DskipTests

FROM eclipse-temurin:21 as runtime
RUN mkdir -p /app
WORKDIR /app
COPY --from=build_stage ./target/facility_rental-0.0.1-SNAPSHOT.jar ./app.jar
CMD ["sleep", "10"]
CMD ["java", "-jar", "./app.jar"]

EXPOSE 8080


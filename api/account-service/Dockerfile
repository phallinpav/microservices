# syntax=docker/dockerfile:1
FROM maven:3.8.5-openjdk-17-slim AS base
WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:go-offline
COPY src ./src

FROM base AS test
CMD ["mvn", "test"]

FROM base AS dev
CMD ["mvn", "spring-boot:run", "-Dspring-boot.run.profiles=mysql", "-Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000'"]

FROM base AS build
RUN mvn package -Dmaven.test.skip

# preferably any small image that only neccessary to run java like JRE instead of JDK
FROM openjdk:17-slim AS prod
EXPOSE 8080

COPY --from=build /app/target/account-service-*.jar /account-service.jar

CMD ["java", "-jar", "/account-service.jar"]

FROM gradle:7.6.2-jdk17 AS builder

WORKDIR /build

COPY ./src ./src
COPY ./PluginAPI/src ./PluginAPI/src
COPY ./PluginAPI/build.gradle ./PluginAPI/build.gradle
COPY ./build.gradle ./build.gradle
COPY ./settings.gradle ./settings.gradle

RUN gradle --no-daemon bootJar

FROM bellsoft/liberica-openjdk-alpine:17.0.7


WORKDIR /app

COPY --from=builder /build/build/libs/EasyContactForms-1.1.0-SNAPSHOT.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "EasyContactForms-1.1.0-SNAPSHOT.jar"]

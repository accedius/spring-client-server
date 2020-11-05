FROM openjdk:14-jdk AS app-build

ENV GRADLE_OPTS -Dorg.gradle.daemon = false
COPY . /build
WORKDIR /build
RUN chmod +x ./gradlew
RUN ./gradlew build

FROM openjdk:14-jdk AS app-build

COPY . /build
WORKDIR /build
RUN chmod +x ./gradlew
RUN ./gradlew build

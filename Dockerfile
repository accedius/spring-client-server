FROM openjdk:14-jdk AS app-build

COPY ./modules/server /build
WORKDIR /build
RUN chmod +x ./gradlew
RUN ./gradlew init
RUN ./gradlew build
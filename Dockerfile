FROM openjdk:14-jdk AS app-build

COPY ./modules/ /build
WORKDIR /build
RUN chmod +x ./server/gradlew
RUN ./server/gradlew clean
RUN ./server/gradlew build
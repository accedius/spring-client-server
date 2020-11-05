FROM openjdk:14-jdk AS app-build

COPY ./modules/server /build
WORKDIR /build
RUN chmod +x .gradlew
RUN .gradlew tasks --all
RUN .gradlew init
RUN .gradlew build
RUN .gradlew tasks --all
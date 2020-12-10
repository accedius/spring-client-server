FROM openjdk:14-jdk AS app-build

COPY ./modules/model /model-lib-install
WORKDIR /model-lib-install
RUN chmod +x ./gradlew
RUN ./gradlew init
RUN ./gradlew install

WORKDIR /

COPY ./modules/server /build
WORKDIR /build
RUN chmod +x ./gradlew
RUN ./gradlew init
RUN ./gradlew build
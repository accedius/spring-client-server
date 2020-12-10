FROM openjdk:14-jdk AS app-build

COPY ./modules/model builds/model
WORKDIR builds/model
RUN chmod +x ./gradlew
RUN ./gradlew init
RUN ./gradlew install

WORKDIR /

COPY ./modules/server builds/server
WORKDIR builds/server
RUN chmod +x ./gradlew
RUN ./gradlew init
RUN ./gradlew build

WORKDIR /

COPY ./modules/client builds/client
WORKDIR builds/client
RUN chmod +x ./gradlew
RUN ./gradlew init
RUN ./gradlew build
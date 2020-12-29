FROM openjdk:14-jdk AS app-build

COPY . build

WORKDIR /build/modules/model
RUN chmod +x ./gradlew
RUN ./gradlew init
RUN ./gradlew install

WORKDIR /build
RUN chmod +x ./gradlew
RUN ./gradlew init
RUN ./gradlew install -p modules/entity

WORKDIR /build/modules/server
RUN chmod +x ./gradlew
RUN ./gradlew init
RUN ./gradlew build

WORKDIR /build/modules/client
RUN chmod +x ./gradlew
RUN ./gradlew init
RUN ./gradlew build
#!/usr/bin/env sh

./gradlew init -p modules/model
./gradlew init -p modules/server
./gradlew init -p modules/client

./gradlew install -p modules/model
./gradlew build -p modules/server
./gradlew build -p modules/client

echo 
echo
echo -e "\e[1;32mPROJECT BUILD SUCCESSFUL\e[0m: all modules have been initialized and built"
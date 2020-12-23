# TJV sem

## Domain Model

![domain-model](resources/DomainModel.png)

## Quick Start Guide

### Execute following steps, if using linux terminal:

- Navigate to the root directory of the project
- Run command `./project-build.sh`
- To run server run command `./run-server.sh`
- To run client run command `./run-client.sh`

### Execute following steps, if using explorer:

- Open the root directory of the project
- Execute `project-build.sh`
- Execute `run-server.sh`
- Execute `run-client.sh`

### Alternatively you can start IntellijIDEA and:

- Run gradle task `Install` on `model` project at subdirectory `modules/model`
- Run `ServerApp` in project `server` at subdirectory `modules/server`
- Run `ClientApp` in project `client` at subdirectory `modules/client` within `Terminal` and give it appropriate arguments as listed in `Client` section below

## Server

Server uses CTU FIT's OracleDB connection, so where is no need to configure anything db related, it should work just fine out of the box (hope so).

## Client


## Problem Solving

If server application is throwing an error, that wanted port is already in use:

- At `server` project files at `src/main/resources/application.properties` change value `server.port = 8080` to `8081` or any other port number, compliant to your system port notation. Do not forget to change `client`'s `application.properties` accordingly, the file is located similarly.
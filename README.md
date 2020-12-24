# TJV sem

## Domain Model

![domain-model](resources/DomainModel.png)

## Quick Start Guide

### Execute following steps, if using linux terminal:

- Navigate to the root directory of the project
- Switch to branch `kb3` or later
- Execute `build-project.sh`
- To run server execute `run-server.sh`

### Execute following steps, if using explorer:

- Open the root directory of the project
- Switch to branch `kb3` or later using any git client (e.g. GitKraken)
- Execute `build-project.sh`
- Execute `run-server.sh`

### Alternatively you can start IntellijIDEA and:

- Switch to branch `kb3` or later in VCS settings of the directory
- Run gradle task `Install` on `model` project at subdirectory `modules/model`
- Run `ServerApp` in project `server` at subdirectory `modules/server`

## Server

Server uses CTU FIT's OracleDB connection, so where is no need to configure anything db related, it should work just fine out of the box (hope so).

## Client

- To run client run command `java -jar modules/client/build/libs/client-0.1-SNAPSHOT.jar` with wanted arguments

Or

- Start IntellijIDEA and run `ClientApp` in project `client` at subdirectory `modules/client` within `Terminal` and give it appropriate arguments as listed in `Client` section below

### Basic arguments:

- Use `--entity=<wanted-entity>` to operate with entity of type `<wanted-entity>`
- Use `--valueParameter=<value>` to pass simple parameter (e.g. Integer, String etc.)
- Use `--complexParameter=<value> --complexParameter=<value> ...` to pass multiple parameters to complex parameter (i.e. authorIds for Work entity etc.)
- `*` means non-required 
- `c` means complex

### Basic commands:

- `--action=create ...`
- `--action=read id=<id>`
- `--action=update id=<id> ...`
- `--action=delete id=<id>`
- `--action=readAll`
- `--action=pageAll`

### Available commands per entity:

#### Student:

Use `--entity=Student`

- `--username`
- `--name`
- `--birthdate`*
- `--averageGrade`*
- `--workIds`*c

Special commands:

- `--action=readByUsername username=<username>`
- `--action=readAllByName name=<name>`
- `--action=joinWork studentId=<id1> workId=<id2>`

#### Teacher:

Use `--entity=Teacher`

- `--username`
- `--name`
- `--birthdate`*
- `--wage`*
- `--assessmentIds`*c

Special commands:

- `--action=readByUsername username=<username>`
- `--action=readAllByName name=<name>`

#### Work:

Use `--entity=Work`

- `--title`
- `--text`*
- `--authorIds`c
- `--assessmentId`*

Special commands:

- `--action=readAllByTitle title=<title>`

#### Assessment:

Use `--entity=Assessment`

- `--grade`
- `--workId`
- `--evaluatorId`

Special commands:

- `--action=readAllByEvaluatorId evaluatorId=<evaluatorId>`

## Problem Solving

If server application is throwing an error, that wanted port is already in use:

- At `server` project files at `src/main/resources/application.properties` change value `server.port = 8080` to `8081` or any other port number, compliant to your system port notation.
- Do not forget to change `client`'s `application.properties` accordingly, the file is located similarly.

Client application may close slowly due to dependency on Oracle DB, but after seeing results (when it's stuck on `Initialized JPA EntityManagerFactory for persistence unit 'default'`) you may close it with `Ctrl + C` or similar combination, depending on your system. It will still finish, but as i mentioned before it will take some time. For the reason see `application.properties` at `client` module. 
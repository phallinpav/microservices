# microservices ( Social Media )

![structure](.github/badges/jacoco.svg)

**Social Media** like friend and chatting

## Feature
1. Add/Remove/Block Friends
2. Chatting message in text or photo/file
3. Posting status > Like/Comment on status

## Structure

**THIS PICTURE IS OUTDATED**, since I have been written **message-service** and **file-service** using C# .NET 6 as learning project. (More detail 
of change is in written text below)
![structure](img/structure.png)

- **ui/frontend**
    - **framework** : nuxtjs + vuetify
    - **features**:
        - login/logout
        - register account
        - update profile
        - add/remove/block friend
        - add/update/remove post
        - like/comment on post
        - chat between friends

- **api gateway**
    - **Kong - api gateway**:
        - load balancing: separate workload between each service
        - rate limiting: limit requesting 
        - proxy caching: cache response entity of request to reduce latency

- **api/backend**
    - **account-service** ( crud account, friend, login/logout security )
        - **framework** : spring boot 2.6.7 - java17
        - **database**: MySQL8 ( using jpa & hibernate to create database and file and insert/delete record )
    - **message-service** ( send, receive, remove message )
        - **framework**: ~~spring boot 2.6.7 - java17~~  C# .NET 6
        - **database**: MongoDB
        - message type text will store in MongoDB
        - info of account will request to account-service
        - message type file ( photo/video ) will request to file-service
        - Socket is being used to give interaction real time
    
    - **post-service** ( upload/remove post, like and comment ) ( NOT YET IMPLEMENT )
        - **framework**: spring boot 2.6.7 - java17
        - **database**: MySQL8
    - **file-service**
        - **framework**: ~~spring boot - java17~~ C# .NET6
        - **file-storage-system**: ~~( CEPH )~~ DropBox API
        - all images and videos like profile picture or message photos between friends are store inside this service

### How to run with docker
- docker-compose in `microservices/` include both docker from ui and api
- `cd ./microservices`

#### Running test
- `docker-compose -f docker-compose.test.yml up -d --build`

#### Running development environment
- `docker-compose -f docker-compose.dev.yml --profile kong-setup up -d --build`
- `docker-compose -f docker-compose.dev.yml up -d --build`

- localhost:1337 for kong gui (with user interface to manage kong gateway)
- localhost:8001 for kong gateway admin (to add or update kong configuration)
- localhost:8888 for kong gateway proxy (to access all upstream that was config in kong)
- localhost:4444 for pgadmin ( user: admin@admin.com, password: root )
- localhost:3000 for ui

#### Running production environment
- `docker-compose -f docker-compose.prod.yml up -d --build`

#### How to build or up service individually using docker-compose:
- up and build ui: `docker-compose -f docker-compose.xxx.yml up -d --build ui`

- up and build account-service and mysql: `docker-compose -f docker-compose.xxx.yml up -d --build mysql account-service`

#### How to run Kong Gateway successfully
- kong-migrate must first start build and finish without any error, so that it can execute script to migrate bootstrap to kong-db (Postgre Sql)
- That why we need to first run this command: `docker-compose -f docker-compose.dev.yml --profile kong-setup up -d --build`
- After it successfully built up for the first time, we don't need to execute that setup migration again. We can simply up/start the container.
- See file `./script/kong-setup.sh` to know detail what it will do

#### About Kong GUI
- Create new user and password on first time access
- Create connection to kong: ex: `Name=kong`, `KONG ADMIN URL=http://kong-gateway:8001`

#### To request directly from UI to API without Kong
- `api\account-service\src\main\java\com\mangobyte\accountservice\auth\config\SecurityConfig.java` :open this function `public CorsFilter corsFilter`
- `ui\nuxt.config.js` : change axios baseurl from kong-gateway(localhost:8888) to backend(localhost:8080)
- `ui\plugins\public-api.ts` : check that file comment to see what to use

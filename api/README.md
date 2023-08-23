# API

### Service:
- account-service
- message-service
- post-service
- file-service

### How to run with docker
- docker-compose in `microservices/api` include all services in this folder and database
- `cd ./microservices/api`

#### Running test
- `docker-compose -f docker-compose.test.yml up --build`

#### Running development environment
- `docker-compose -f docker-compose.dev.yml up --build`

##### account-service:
  - How to debug: 
    - IntelliJ-IDE:
      - Run > Edit Configurations...
      - Click + > Remote JVM Debug
        - Name: account-service-debug
        - Debug Mode: Attach to JVM
        - Transport: Socket
        - Host: localhost Port: 8000
        - Command line remote JVM: 
          - `-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000`
        - Use module class: account-service
        - Ok/Apply
      - Run docker-compose.dev yml (`docker-compose -f docker-compose.dev.yml up --build`)
      - IDE: Run > Debug > account-service-debug
        - you can put breakpoint on code you want to debug, and it works the same way as it was in local run.
#### Running production environment
- `docker-compose -f docker-compose.prod.yml up --build`

#### How to build or up service individually using docker-compose:
- up and build account-service: `docker-compose -f docker-compose.xxx.yml up --build account-service`

- up and build mysql and post-service: `docker-compose -f docker-compose.xxx.yml up --build mysql message-service`



#### How to see test code coverage
- `mvn jacoco:report` ( after execute mvn test )
- Go to `/target/site/jacoco/index.html` to see report


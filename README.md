# Sport facility rental system

# This project was developed for *Fundamentals of Network Appliactions* course during 5th semester of Applied Computer Science faculty at Politechnika Łódzka.

This is a simple application allowing to reservate various sport facilities (e. g. football fields, tennis courts, etc.).

Features:
- creating, ending, and cancelling the rents (only not-ended rents can be cancelled)
- adding new facilities
- adding new musers, modifying them, with activate/deactivate option
- overview of details for every client
- Authentication and role-based access control (manager, administrator, client) implemented with Spring Security
- unit tests using JUnit and Testcontainers
- Integration test using RESTAssured

### Tech stack:
#### Backend:
- Java 21
- Spring Boot 3.5
- MongoDB database (version 8.0) 
- Redis database (for cache, version 7.0)
- Both databases are provided via Docker containers 

#### Front-end:
- React 19.2.5 using TypeScript
- TailwindCSS 4.1.8

## Installation and running guide:
Make sure following tools are installed on your machine:
- node.js 22+
- JDK 21+
- Docker 3+ (with WSL if you're using Windows)

To run server-side application, do the following steps:
- create and run the Docker containers:
`cd ./Docker_single`
`docker compose up`
- run whe REST application:
`mvn spring-boot:run`
alternatively you can run the app through JAR:
`mvn clean package`
```bash
cd ./target
java -jar facility_rental-0.0.1-SNAPSHOT.jar
```

To run the client application use npm:
```bash
cd ./spa
npm run dev
```

Repository also contains:
`mvc` branch - simple MVC web applicatiopn for prevoius version of backend (with no auth)
`quarkus` branch - REST application remade to the Quarkus application
`mobile` directory - mobile application made basing on React web application using React Native

### In development/marked to fix:
- adjusting the integration tests according to the addition of security features (authentication and access control)
- investinagting the issue with replica-set cluster Mongo version and making it primary choice for database for the app in the future
- complete remaking the UI appearance and styling, and also the responsibility for mobile devices
- further cleanups and adjustments

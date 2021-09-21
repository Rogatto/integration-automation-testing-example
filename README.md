# Sample REST CRUD API with Spring Boot, Mysql, JPA and Hibernate 

## Steps to Setup



**1. Build and run the app using maven**

```bash
mvn package
java -jar target/spring-boot-rest-api-tutorial-0.0.1-SNAPSHOT.jar

```

Alternatively, you can run the app without packaging it using -

```bash
mvn spring-boot:run
```

The app will start running at <http://localhost:8080>.

## Explore Rest APIs

The app defines following CRUD APIs.

    GET /api/v1/users
    
    POST /api/v1/users
    
    GET /api/v1/users/{userId}
    
    PUT /api/v1/users/{userId}
    
    DELETE /api/v1/users/{userId}


<br/>

**2. Sonar Qube with docker compose**
(before go to directory docker-sonarqube)

```bash
docker-compose.yml up -d
```

**3. Execute test with code coverage Jacoco - * remember to start docker engine**
(the code coverage report will appear in target/site/jacoco)

```bash
mvn clean install
```

**4. Postman collection -> API-USER.postman_collection.json**
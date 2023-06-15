## Requirements

For building and running the application you need:

- [JDK 17](https://www.oracle.com/java/technologies/downloads/#java17)
- [Maven 3](https://maven.apache.org)
- [Node](https://nodejs.org/en) (This is optional since the angular frontend ui is already build and located at main/resources/public)


## Running the application locally with dockerize: postgres and fakesmtp

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.marktoledo.pccwexam.PccwExamApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

For Frontend its used Angular 13. It's already build and it`s located at main/resources/public  but if you want to check and build locally

```shell
cd frontend-angular-app
```

```shell
npm install
```

```shellvvvf,zz
npm build
```

Angular build will overwrite the main/resources/public

## Running the application on docker
It will use the same postgres and fakesms container

```shell
docker-compose -f compose-all.yaml up -d
```



#maven
FROM maven:3.9.2-eclipse-temurin-17-alpine AS maven

WORKDIR /usr/src/app
COPY . /usr/src/app

RUN java -version
RUN mvn package -e

FROM amd64/eclipse-temurin:17.0.7_7-jdk-alpine

ARG JAR_FILE=pccw-exam-0.0.1-SNAPSHOT.jar

WORKDIR /opt/app

COPY --from=maven /usr/src/app/target/${JAR_FILE} /opt/app/

ENTRYPOINT ["java","-jar","pccw-exam-0.0.1-SNAPSHOT.jar"]
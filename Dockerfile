FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar KLBStore.jar
ENTRYPOINT ["java","-jar","/KLBStore.jar"]
EXPOSE 8080
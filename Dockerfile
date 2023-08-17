FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp

# Copy your application JAR
COPY target/*.jar KLBStore.jar

# Copy the JDBC driver JAR to the image
COPY mssql-jdbc-12.4.0.jre11.jar /mssql-jdbc.jar

ENTRYPOINT ["java","-cp","/mssql-jdbc.jar:/KLBStore.jar","-jar","/KLBStore.jar"]
EXPOSE 8080

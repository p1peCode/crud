FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY . /app
RUN chmod +x mvnw
RUN ./mvnw clean package
VOLUME /tmp
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
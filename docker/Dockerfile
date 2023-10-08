FROM openjdk:21-oracle as builder
EXPOSE 8080
WORKDIR /app
COPY target/BurSchedule-1.0.jar BurSchedule-1.0.jar
RUN java -Djarmode=layertools -jar BurSchedule-1.0.jar extract
ENTRYPOINT ["java","-jar","/app/BurSchedule-1.0.jar"]

FROM openjdk:21-oracle
COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
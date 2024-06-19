FROM openjdk:17-ea-11-jdk-slim

COPY ./build/libs/dailyband.jar /app.jar

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app.jar"]
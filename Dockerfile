FROM openjdk:17-ea-11-jdk-slim

COPY ./build/libs/dailyband.jar /app.jar

ENV JAVA_OPTS="-Djasypt.encryptor.password=hta1226"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app.jar"]
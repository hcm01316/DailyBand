FROM openjdk:17-ea-11-jdk-slim

COPY ./build/libs/dailyband.jar /app.jar

ENV JAVA_OPTS="-Djasypt.encryptor.password=비밀번호"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app.jar"]
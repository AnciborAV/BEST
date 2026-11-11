# Build the executable Spring Boot JAR with the project's Java version.
FROM maven:3.9.11-eclipse-temurin-25 AS build
WORKDIR /build
COPY pom.xml ./
COPY src ./src
# Tests run in GitHub Actions; image builds do not require a database.
RUN mvn --batch-mode --no-transfer-progress -DskipTests package

# Run with a smaller Java runtime and an unprivileged user.
FROM eclipse-temurin:25-jre-noble
WORKDIR /app
RUN groupadd --system opora && useradd --system --gid opora --home-dir /app opora \
    && mkdir -p /app/data && chown -R opora:opora /app
COPY --from=build --chown=opora:opora /build/target/opora-0.0.1-SNAPSHOT.jar /app/app.jar
ENV SERVER_ADDRESS=0.0.0.0 \
    PORT=10000 \
    JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=50.0 -XX:+UseSerialGC -XX:+ExitOnOutOfMemoryError"
USER opora
EXPOSE 10000
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

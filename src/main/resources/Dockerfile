FROM openjdk:8 as build
WORKDIR /workspace/app
EXPOSE 8761

COPY ../../../mvnw .
COPY ../../../.mvn .mvn
COPY ../../../pom.xml .
COPY ../../../src src

RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:8
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","io.nelson.nadar.discoveryserver.DiscoveryServerApplication"]
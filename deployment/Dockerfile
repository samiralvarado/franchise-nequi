FROM gradle:8.5-jdk21 AS build

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle build --no-daemon -x test

FROM eclipse-temurin:21-jre-jammy

USER root
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/* \
    && curl -sS https://truststore.pki.rds.amazonaws.com/us-east-2/us-east-2-bundle.pem -o /tmp/aws-rds-ca.pem \
    && keytool -import -trustcacerts -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -noprompt -alias aws-docdb -file /tmp/aws-rds-ca.pem \
    && rm /tmp/aws-rds-ca.pem

RUN addgroup --system spring && adduser --system --group spring
USER spring:spring

COPY --from=build /home/gradle/src/application/app-service/build/libs/app.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Djavax.net.ssl.trustStorePassword=changeit", "-jar", "app.jar"]
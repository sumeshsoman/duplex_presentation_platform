FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD ./target/presentationplatform-0.0.1-SNAPSHOT.jar  presentationplatform-0.0.1-SNAPSHOT.jar
ENV JAVA_OPTS=""
ENTRYPOINT ["java","-Dserver.servlet.contextPath=/duplex/v1/","-Djava.security.egd=file:/dev/./urandom","-jar","/presentationplatform-0.0.1-SNAPSHOT.jar"]
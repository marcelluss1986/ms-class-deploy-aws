FROM openjdk:17
VOLUME /tmp
EXPOSE 8080
ADD ./target/mscatalog-0.0.1-SNAPSHOT.jar mscatalog.jar
ENTRYPOINT ["java","-jar","/mscatalog.jar"]
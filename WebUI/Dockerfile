FROM java:8-jre

WORKDIR /root
ADD target/*.jar ./

ENTRYPOINT ["java","-jar","WebUI-0.0.1-SNAPSHOT.jar"]

FROM tomcat:10.1-jdk17

COPY target/CarShopService-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/CarShopService-1.0-SNAPSHOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]

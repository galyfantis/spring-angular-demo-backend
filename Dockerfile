FROM lwieske/java-8

RUN adduser -D demo demo

RUN mkdir -p /opt/demo

COPY target/spring-boot-demo*.jar /opt/demo/spring-boot-demo.jar

RUN chown -R demo:demo /opt/demo

RUN chmod 744 /opt/demo/spring-boot-demo.jar

USER demo

WORKDIR /opt/demo

EXPOSE 8080

ENTRYPOINT java $JAVA_OPTS -jar spring-boot-demo.jar

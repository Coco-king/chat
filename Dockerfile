FROM java:8
EXPOSE 9000

VOLUME /tmp

ENV TZ=Asia/Shanghai
RUN ln -sf /usr/share/zoneinfo/{TZ} /etc/localtime && echo "{TZ}" > /etc/timezone

ADD chat-0.0.1-SNAPSHOT.jar /app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","/app.jar"]

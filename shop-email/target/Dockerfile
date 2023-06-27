FROM java:8

COPY *.jar /app.jar

CMD ["--server.port=9999"]

EXPOSE 9999

ENTRYPOINT ["java","-jar","/app.jar"]
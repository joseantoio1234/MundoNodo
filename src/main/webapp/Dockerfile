FROM tomcat:10-jre17

RUN rm -rf /usr/local/tomcat/webapps/ROOT

COPY "./Web Pages" /usr/local/tomcat/webapps/ROOT

EXPOSE 8080

CMD ["catalina.sh", "run"]

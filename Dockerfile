# 1. Usamos una imagen de Tomcat oficial
FROM tomcat:10-jre17

# 2. Eliminamos la carpeta ROOT por defecto por completo
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# 3. Copiamos tu carpeta webapp y hacemos que SEA la nueva ROOT
COPY ./src/main/webapp /usr/local/tomcat/webapps/ROOT

# 4. Exponemos el puerto
EXPOSE 8080

# 5. Arrancamos Tomcat
CMD ["catalina.sh", "run"]
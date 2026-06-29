# 1. Usamos una imagen de Tomcat oficial
FROM tomcat:10-jre17

# 2. Eliminamos las aplicaciones por defecto de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# 3. Copiamos el contenido de la carpeta webapp a la raíz de Tomcat
COPY ./src/main/webapp /usr/local/tomcat/webapps/ROOT

# 4. Exponemos el puerto
EXPOSE 8080

# 5. Arrancamos Tomcat
CMD ["catalina.sh", "run"]

# Estágio 1: Build com Maven
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn clean package

# Estágio 2: Imagem Final com Tomcat
FROM tomcat:10.1-jdk17
WORKDIR /usr/local/tomcat/webapps

# Remove o conteúdo padrão do Tomcat.
RUN rm -rf ROOT

# Copia o arquivo .war que o Maven gerou para a pasta de webapps do Tomcat.
COPY --from=build /app/target/docflow-web.war .

# Expõe a porta 8080.
EXPOSE 8080

# Comando para iniciar o Tomcat.
CMD ["catalina.sh", "run"]
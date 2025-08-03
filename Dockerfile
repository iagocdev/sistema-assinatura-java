# Estágio 1: Build com Maven
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package

# Estágio 2: Imagem Final com Tomcat
FROM tomcat:10.1-jdk17

# Copia o .war completo gerado pelo Maven para a pasta webapps do Tomcat, 
# renomeando para ROOT.war para a URL ficar mais limpa.
COPY --from=build /app/target/docflow-web.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
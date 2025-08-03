# Estágio 1: Build com Maven
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package

# ----- LINHA DE DEPURAÇÃO ADICIONADA ABAIXO -----
# Lista o conteúdo do .war gerado para podermos ver no log do deploy.
RUN unzip -l /app/target/docflow-web.war

# Estágio 2: Imagem Final com Tomcat
FROM tomcat:10.1-jdk17

# Copia o .war gerado para a pasta webapps do Tomcat, renomeando para ROOT.war
COPY --from=build /app/target/docflow-web.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
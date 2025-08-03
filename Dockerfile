# Estágio 1: Build com Maven
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
# Apenas compila o código, não gera mais o .war
RUN mvn compile

# Estágio 2: Imagem Final com Tomcat
FROM tomcat:10.1-jdk17

# Define o diretório de trabalho como a aplicação ROOT
WORKDIR /usr/local/tomcat/webapps/ROOT

# Copia os arquivos HTML, CSS, etc. para a raiz da aplicação
COPY --from=build /app/src/main/webapp/ .

# Copia as bibliotecas (.jar) para a pasta WEB-INF/lib
COPY --from=build /app/target/dependency/. ./WEB-INF/lib/

# Copia as classes Java compiladas (.class) para a pasta WEB-INF/classes
COPY --from=build /app/target/classes/. ./WEB-INF/classes/

EXPOSE 8080
CMD ["catalina.sh", "run"]
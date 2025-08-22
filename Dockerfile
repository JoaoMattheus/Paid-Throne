# Multi-stage build para otimizar o tamanho da imagem final
FROM maven:3.9.4-eclipse-temurin-17 as build

# Define o diretório de trabalho
WORKDIR /app

# Copia apenas os arquivos necessários para o build (otimiza cache do Docker)
COPY pom.xml .
COPY src ./src

# Executa o build da aplicação (skip tests para agilizar - opcional)
RUN mvn clean package -DskipTests

# Stage final - imagem mais leve
FROM eclipse-temurin:17-jre-alpine

# Cria um usuário não-root para segurança
RUN addgroup -g 1001 -S appgroup && \
    adduser -S appuser -u 1001 -G appgroup

# Define o diretório de trabalho
WORKDIR /app

# Copia o JAR compilado do stage anterior
COPY --from=build /app/target/calculator-0.0.1-SNAPSHOT.jar calculator.jar

# Ajusta as permissões
RUN chown appuser:appgroup calculator.jar

# Muda para o usuário não-root
USER appuser

# Define variáveis de ambiente padrão (podem ser sobrescritas)
ENV PORT=8080
ENV SPRING_PROFILES_ACTIVE=prod

# Expõe a porta
EXPOSE ${PORT}

# Configurações da JVM para containers
ENV JAVA_OPTS="-Xms256m -Xmx512m -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}"

# Comando para iniciar a aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar calculator.jar"]
# Configuração de Deploy Automático - Paid Throne

## Informações do Projeto
- **Nome**: Paid Throne
- **Tipo**: Spring Boot Application
- **Versão Java**: 17
- **Porta**: 8080
- **Contexto**: /calculator

## Arquivos Docker
- `Dockerfile`: Configuração multi-stage para build otimizado
- `docker-compose.yml`: Orquestração completa com PostgreSQL e Adminer
- `.dockerignore`: Otimização do contexto de build

## Variáveis de Ambiente Necessárias
```bash
PORT=8080
DB_URL=jdbc:postgresql://db:5432/dbptk
DB_USERNAME=thorone
DB_PASSWORD=gA3ifLgwMwo8EN4aBR98AIGygEiVbkFN
SPRING_PROFILES_ACTIVE=prod
```

## Serviços
1. **app**: Aplicação Spring Boot (porta 8080)
2. **db**: PostgreSQL 15 Alpine (porta 5432)
3. **adminer**: Interface de administração do banco (porta 8081)

## Health Checks
- App: `http://localhost:8080/calculator/actuator/health`
- DB: `pg_isready -U thorone -d dbptk`

## Comandos de Deploy
```bash
# Build e start
docker-compose up -d --build

# Verificar logs
docker-compose logs -f app

# Status
docker-compose ps
```

## Endpoints
- **Aplicação**: http://localhost:8080/calculator
- **Health Check**: http://localhost:8080/calculator/actuator/health  
- **Adminer**: http://localhost:8081

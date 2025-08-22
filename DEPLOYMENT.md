# 🚀 Deployment Guide - Paid Throne

## 📋 Pré-requisitos

- Docker e Docker Compose instalados no VPS
- Portas 8080 e 5432 disponíveis
- Pelo menos 1GB de RAM disponível

## 🔧 Configuração

### Variáveis de Ambiente

As seguintes variáveis podem ser configuradas no `docker-compose.yml`:

- `PORT`: Porta da aplicação (padrão: 8080)
- `DB_URL`: URL do banco PostgreSQL
- `DB_USERNAME`: Usuário do banco
- `DB_PASSWORD`: Senha do banco
- `SPRING_PROFILES_ACTIVE`: Perfil do Spring (padrão: prod)

## 🚀 Como fazer o deploy

### Método 1: Usando script automatizado

**Linux/Mac:**
```bash
chmod +x deploy.sh
./deploy.sh
```

**Windows PowerShell:**
```powershell
.\deploy.ps1
```

### Método 2: Comandos manuais

```bash
# 1. Parar containers existentes
docker-compose down

# 2. Construir a imagem
docker-compose build

# 3. Iniciar os serviços
docker-compose up -d

# 4. Verificar status
docker-compose ps
```

## 🔍 Verificação

Após o deploy, verifique se tudo está funcionando:

- **Aplicação**: http://localhost:8080/calculator
- **Health Check**: http://localhost:8080/calculator/actuator/health
- **Adminer**: http://localhost:8081

## 📊 Monitoramento

```bash
# Ver logs da aplicação
docker-compose logs -f app

# Ver logs do banco
docker-compose logs -f db

# Ver status dos containers
docker-compose ps

# Verificar uso de recursos
docker stats
```

## 🛠️ Troubleshooting

### Aplicação não inicia

```bash
# Verificar logs
docker-compose logs app

# Verificar se o banco está rodando
docker-compose exec db pg_isready -U thorone -d dbptk
```

### Problema de conexão com banco

```bash
# Verificar network
docker network ls

# Testar conectividade
docker-compose exec app ping db
```

### Limpar tudo e recomeçar

```bash
# Parar tudo
docker-compose down -v

# Remover imagens
docker rmi $(docker images "paid-throne*" -q)

# Rebuild completo
docker-compose build --no-cache
docker-compose up -d
```

## 📝 Backup do Banco

```bash
# Backup
docker-compose exec db pg_dump -U thorone dbptk > backup.sql

# Restore
docker-compose exec -T db psql -U thorone dbptk < backup.sql
```

## 🔒 Segurança para Produção

1. **Altere as senhas padrão** no `docker-compose.yml`
2. **Configure um reverse proxy** (Nginx/Apache) se necessário
3. **Use HTTPS** em produção
4. **Configure firewall** para limitar acesso às portas
5. **Considere usar Docker Secrets** para senhas sensíveis

## 📈 Otimizações para Produção

1. **Ajuste as configurações de memória JVM** no Dockerfile
2. **Configure logs estruturados**
3. **Use um orchestrador** como Docker Swarm ou Kubernetes para alta disponibilidade
4. **Configure monitoramento** com Prometheus/Grafana

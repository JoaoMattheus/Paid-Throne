#!/bin/bash

# Script de deployment para VPS
# Uso: ./deploy.sh [environment]

set -e

ENVIRONMENT=${1:-prod}
APP_NAME="paid-throne"

echo "🚀 Iniciando deployment da aplicação $APP_NAME..."
echo "📍 Ambiente: $ENVIRONMENT"

# Parar containers existentes
echo "🛑 Parando containers existentes..."
docker-compose down --remove-orphans || true

# Remover imagens antigas (opcional - descomente se quiser forçar rebuild)
# docker rmi $(docker images "$APP_NAME*" -q) || true

# Build da nova imagem
echo "🔨 Construindo nova imagem..."
docker-compose build --no-cache

# Iniciar os serviços
echo "🚀 Iniciando serviços..."
docker-compose up -d

# Verificar se os containers estão rodando
echo "🔍 Verificando status dos containers..."
docker-compose ps

# Aguardar a aplicação ficar disponível
echo "⏳ Aguardando aplicação ficar disponível..."
sleep 30

# Verificar health check
echo "🏥 Verificando saúde da aplicação..."
if curl -f http://localhost:8080/calculator/actuator/health > /dev/null 2>&1; then
    echo "✅ Aplicação está rodando corretamente!"
    echo "🌐 Aplicação disponível em: http://localhost:8080/calculator"
    echo "🗄️  Adminer disponível em: http://localhost:8081"
else
    echo "❌ Falha no health check. Verificando logs..."
    docker-compose logs app
    exit 1
fi

echo "🎉 Deploy concluído com sucesso!"

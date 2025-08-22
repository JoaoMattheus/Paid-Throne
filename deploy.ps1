# Script de deployment PowerShell para Windows
# Uso: .\deploy.ps1 [environment]

param(
    [string]$Environment = "prod"
)

$ErrorActionPreference = "Stop"
$APP_NAME = "paid-throne"

Write-Host "🚀 Iniciando deployment da aplicação $APP_NAME..." -ForegroundColor Green
Write-Host "📍 Ambiente: $Environment" -ForegroundColor Yellow

try {
    # Parar containers existentes
    Write-Host "🛑 Parando containers existentes..." -ForegroundColor Yellow
    docker-compose down --remove-orphans 2>$null

    # Build da nova imagem
    Write-Host "🔨 Construindo nova imagem..." -ForegroundColor Yellow
    docker-compose build --no-cache

    # Iniciar os serviços
    Write-Host "🚀 Iniciando serviços..." -ForegroundColor Yellow
    docker-compose up -d

    # Verificar se os containers estão rodando
    Write-Host "🔍 Verificando status dos containers..." -ForegroundColor Yellow
    docker-compose ps

    # Aguardar a aplicação ficar disponível
    Write-Host "⏳ Aguardando aplicação ficar disponível..." -ForegroundColor Yellow
    Start-Sleep -Seconds 30

    # Verificar health check
    Write-Host "🏥 Verificando saúde da aplicação..." -ForegroundColor Yellow
    try {
        $response = Invoke-WebRequest -Uri "http://localhost:8080/calculator/actuator/health" -Method Get -TimeoutSec 10
        if ($response.StatusCode -eq 200) {
            Write-Host "✅ Aplicação está rodando corretamente!" -ForegroundColor Green
            Write-Host "🌐 Aplicação disponível em: http://localhost:8080/calculator" -ForegroundColor Cyan
            Write-Host "🗄️  Adminer disponível em: http://localhost:8081" -ForegroundColor Cyan
        }
    }
    catch {
        Write-Host "❌ Falha no health check. Verificando logs..." -ForegroundColor Red
        docker-compose logs app
        exit 1
    }

    Write-Host "🎉 Deploy concluído com sucesso!" -ForegroundColor Green
}
catch {
    Write-Host "❌ Erro durante o deployment: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

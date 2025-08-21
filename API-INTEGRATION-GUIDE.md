# 🔧 Guia de Integração - Paid Throne Calculator API

## 📋 Índice
- [Configuração Rápida](#configuração-rápida)
- [Exemplos por Linguagem](#exemplos-por-linguagem)
- [Casos de Uso Comuns](#casos-de-uso-comuns)
- [Tratamento de Erros](#tratamento-de-erros)
- [Boas Práticas](#boas-práticas)
- [Monitoramento](#monitoramento)

## 🚀 Configuração Rápida

### 1. Variáveis de Ambiente
```bash
# .env
PORT=8080
DB_URL=jdbc:postgresql://localhost:5432/paid_throne_db
DB_USERNAME=postgres
DB_PASSWORD=postgres
```

### 2. Docker Compose
```yaml
version: '3.8'
services:
  paid-throne-api:
    image: paid-throne:latest
    ports:
      - "8080:8080"
    environment:
      - PORT=8080
      - DB_URL=jdbc:postgresql://postgres:5432/paid_throne_db
      - DB_USERNAME=postgres
      - DB_PASSWORD=postgres
    depends_on:
      - postgres
      
  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: paid_throne_db
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - "5432:5432"

volumes:
  postgres_data:
```

### 3. Testando a API
```bash
# Health check
curl http://localhost:8080/calculator/actuator/health

# Swagger UI
http://localhost:8080/calculator/swagger-ui/index.html
```

## 🌐 Exemplos por Linguagem

### JavaScript/TypeScript

#### Configuração Base
```typescript
interface ApiConfig {
  baseUrl: string;
  timeout: number;
}

interface King {
  username: string;
  averageBathroomTime: number;
  numberOfVisitsPerDay: number;
  salary: number;
  salaryType: 'HOURLY' | 'DAILY' | 'MONTHLY' | 'YEARLY';
  workSchedule: 'SIX_ON_ONE' | 'FIVE_ON_TWO' | 'FOUR_ON_THREE' | 'TWELVE_ON_THIRTY_SIX';
}

interface CalculateResponse {
  username: string;
  dailyMinutesSpent: number;
  monthlyMinutesSpent: number;
  yearlyMinutesSpent: number;
  dailyEarnings: number;
  monthlyEarnings: number;
  yearlyEarnings: number;
  dailyPercentageOfShift: number;
}

class PaidThroneApi {
  private config: ApiConfig;

  constructor(config: ApiConfig) {
    this.config = config;
  }

  async calculate(king: King): Promise<CalculateResponse> {
    try {
      const response = await fetch(`${this.config.baseUrl}/v1/calculate`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(king),
      });

      if (!response.ok) {
        const error = await response.json();
        throw new Error(`API Error ${response.status}: ${error.message}`);
      }

      return await response.json();
    } catch (error) {
      console.error('Error calculating earnings:', error);
      throw error;
    }
  }

  async getRanking(type: 'HIGHER_EARNINGS' | 'HIGHER_MINUTES' | 'LOWER_MINUTES' = 'HIGHER_EARNINGS') {
    try {
      const response = await fetch(`${this.config.baseUrl}/v1/ranking?type=${type}`);
      
      if (!response.ok) {
        throw new Error(`Failed to fetch ranking: ${response.statusText}`);
      }

      return await response.json();
    } catch (error) {
      console.error('Error fetching ranking:', error);
      throw error;
    }
  }

  async getStatistics() {
    try {
      const response = await fetch(`${this.config.baseUrl}/v1/statistic`);
      
      if (!response.ok) {
        throw new Error(`Failed to fetch statistics: ${response.statusText}`);
      }

      return await response.json();
    } catch (error) {
      console.error('Error fetching statistics:', error);
      throw error;
    }
  }
}
```

#### Exemplo de Uso
```typescript
const api = new PaidThroneApi({
  baseUrl: 'http://localhost:8080/calculator',
  timeout: 5000
});

async function exemploCompleto() {
  try {
    // 1. Calcular ganhos
    const king: King = {
      username: 'DevMaster',
      averageBathroomTime: 10,
      numberOfVisitsPerDay: 3,
      salary: 8000,
      salaryType: 'MONTHLY',
      workSchedule: 'FIVE_ON_TWO'
    };

    const earnings = await api.calculate(king);
    console.log('💰 Ganhos calculados:', earnings);

    // 2. Buscar ranking
    const topEarners = await api.getRanking('HIGHER_EARNINGS');
    console.log('🏆 Top earners:', topEarners);

    // 3. Ver estatísticas gerais
    const stats = await api.getStatistics();
    console.log('📊 Estatísticas do reino:', stats);

  } catch (error) {
    console.error('❌ Erro na integração:', error);
  }
}

exemploCompleto();
```

### Python

#### Cliente Python
```python
import requests
import json
from typing import Optional, Dict, Any, List
from dataclasses import dataclass
from enum import Enum

class SalaryType(Enum):
    HOURLY = "HOURLY"
    DAILY = "DAILY"
    MONTHLY = "MONTHLY"
    YEARLY = "YEARLY"

class WorkSchedule(Enum):
    SIX_ON_ONE = "SIX_ON_ONE"
    FIVE_ON_TWO = "FIVE_ON_TWO"
    FOUR_ON_THREE = "FOUR_ON_THREE"
    TWELVE_ON_THIRTY_SIX = "TWELVE_ON_THIRTY_SIX"

class RankingType(Enum):
    HIGHER_EARNINGS = "HIGHER_EARNINGS"
    HIGHER_MINUTES = "HIGHER_MINUTES"
    LOWER_MINUTES = "LOWER_MINUTES"

@dataclass
class King:
    username: str
    average_bathroom_time: int
    number_of_visits_per_day: int
    salary: float
    salary_type: SalaryType
    work_schedule: WorkSchedule

    def to_dict(self) -> Dict[str, Any]:
        return {
            "username": self.username,
            "averageBathroomTime": self.average_bathroom_time,
            "numberOfVisitsPerDay": self.number_of_visits_per_day,
            "salary": self.salary,
            "salaryType": self.salary_type.value,
            "workSchedule": self.work_schedule.value
        }

class PaidThroneApiClient:
    def __init__(self, base_url: str = "http://localhost:8080/calculator"):
        self.base_url = base_url
        self.session = requests.Session()
        self.session.headers.update({
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        })

    def calculate_earnings(self, king: King) -> Dict[str, Any]:
        """Calcula ganhos no banheiro para um usuário"""
        url = f"{self.base_url}/v1/calculate"
        
        try:
            response = self.session.post(url, json=king.to_dict())
            response.raise_for_status()
            return response.json()
        except requests.exceptions.HTTPError as e:
            if response.status_code == 400:
                error_detail = response.json()
                raise ValueError(f"Dados inválidos: {error_detail.get('message', 'Erro desconhecido')}")
            else:
                raise Exception(f"Erro HTTP {response.status_code}: {e}")
        except requests.exceptions.RequestException as e:
            raise Exception(f"Erro de conexão: {e}")

    def get_ranking(self, ranking_type: RankingType = RankingType.HIGHER_EARNINGS) -> List[Dict[str, Any]]:
        """Busca ranking dos usuários"""
        url = f"{self.base_url}/v1/ranking"
        params = {"type": ranking_type.value}
        
        try:
            response = self.session.get(url, params=params)
            response.raise_for_status()
            return response.json()
        except requests.exceptions.RequestException as e:
            raise Exception(f"Erro ao buscar ranking: {e}")

    def get_statistics(self) -> Dict[str, Any]:
        """Busca estatísticas gerais"""
        url = f"{self.base_url}/v1/statistic"
        
        try:
            response = self.session.get(url)
            response.raise_for_status()
            return response.json()
        except requests.exceptions.RequestException as e:
            raise Exception(f"Erro ao buscar estatísticas: {e}")

# Exemplo de uso
if __name__ == "__main__":
    api = PaidThroneApiClient()
    
    # Criar usuário
    developer = King(
        username="PythonDev",
        average_bathroom_time=12,
        number_of_visits_per_day=2,
        salary=10000.0,
        salary_type=SalaryType.MONTHLY,
        work_schedule=WorkSchedule.FIVE_ON_TWO
    )
    
    try:
        # Calcular ganhos
        earnings = api.calculate_earnings(developer)
        print("💰 Ganhos calculados:")
        print(json.dumps(earnings, indent=2, ensure_ascii=False))
        
        # Buscar ranking
        top_earners = api.get_ranking(RankingType.HIGHER_EARNINGS)
        print("\n🏆 Top 5 maiores ganhos:")
        for i, user in enumerate(top_earners, 1):
            print(f"{i}. {user['username']}: R$ {user['dailyEarnings']:.2f}/dia")
        
        # Estatísticas gerais
        stats = api.get_statistics()
        print(f"\n📊 Reino tem {stats['totalKings']} usuários")
        print(f"💸 Total de ganhos anuais: R$ {stats['totalYearlyEarnings']:,.2f}")
        
    except Exception as e:
        print(f"❌ Erro: {e}")
```

### Java/Spring Boot

#### Cliente Java
```java
@Component
@Slf4j
public class PaidThroneApiClient {
    
    private final RestTemplate restTemplate;
    private final String baseUrl;
    
    public PaidThroneApiClient(@Value("${paid-throne.api.base-url}") String baseUrl) {
        this.baseUrl = baseUrl;
        this.restTemplate = new RestTemplate();
        
        // Configurar timeout
        var requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(10000);
        restTemplate.setRequestFactory(requestFactory);
    }
    
    public KingCalculateResponse calculateEarnings(King king) {
        String url = baseUrl + "/v1/calculate";
        
        try {
            log.info("Calculando ganhos para usuário: {}", king.username());
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<King> request = new HttpEntity<>(king, headers);
            
            ResponseEntity<KingCalculateResponse> response = restTemplate.postForEntity(
                url, request, KingCalculateResponse.class);
            
            log.info("Cálculo realizado com sucesso para: {}", king.username());
            return response.getBody();
            
        } catch (HttpClientErrorException.BadRequest e) {
            log.error("Dados inválidos para usuário {}: {}", king.username(), e.getResponseBodyAsString());
            throw new IllegalArgumentException("Dados inválidos: " + e.getResponseBodyAsString());
            
        } catch (RestClientException e) {
            log.error("Erro ao calcular ganhos para {}: {}", king.username(), e.getMessage());
            throw new RuntimeException("Erro na comunicação com a API", e);
        }
    }
    
    public List<RankingKingResponse> getRanking(RankingType type) {
        String url = baseUrl + "/v1/ranking?type=" + type.name();
        
        try {
            log.info("Buscando ranking do tipo: {}", type);
            
            ResponseEntity<RankingKingResponse[]> response = restTemplate.getForEntity(
                url, RankingKingResponse[].class);
            
            List<RankingKingResponse> ranking = Arrays.asList(response.getBody());
            log.info("Ranking obtido com {} entradas", ranking.size());
            
            return ranking;
            
        } catch (RestClientException e) {
            log.error("Erro ao buscar ranking {}: {}", type, e.getMessage());
            throw new RuntimeException("Erro ao buscar ranking", e);
        }
    }
    
    public KingdomStatisticResponse getStatistics() {
        String url = baseUrl + "/v1/statistic";
        
        try {
            log.info("Buscando estatísticas do reino");
            
            ResponseEntity<KingdomStatisticResponse> response = restTemplate.getForEntity(
                url, KingdomStatisticResponse.class);
            
            KingdomStatisticResponse stats = response.getBody();
            log.info("Estatísticas obtidas: {} usuários, {} ganhos totais", 
                stats.totalKings(), stats.totalYearlyEarnings());
            
            return stats;
            
        } catch (RestClientException e) {
            log.error("Erro ao buscar estatísticas: {}", e.getMessage());
            throw new RuntimeException("Erro ao buscar estatísticas", e);
        }
    }
}

// Exemplo de uso em um Controller
@RestController
@RequestMapping("/integration")
@RequiredArgsConstructor
public class IntegrationController {
    
    private final PaidThroneApiClient paidThroneClient;
    
    @PostMapping("/calculate-user")
    public ResponseEntity<KingCalculateResponse> calculateUserEarnings(@RequestBody King king) {
        try {
            KingCalculateResponse result = paidThroneClient.calculateEarnings(king);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardData> getDashboard() {
        try {
            KingdomStatisticResponse stats = paidThroneClient.getStatistics();
            List<RankingKingResponse> topEarners = paidThroneClient.getRanking(RankingType.HIGHER_EARNINGS);
            
            DashboardData dashboard = new DashboardData(stats, topEarners);
            return ResponseEntity.ok(dashboard);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
```

## 💼 Casos de Uso Comuns

### 1. Dashboard Corporativo
```javascript
class CorporateDashboard {
  constructor(apiClient) {
    this.api = apiClient;
    this.refreshInterval = 30000; // 30 segundos
  }

  async loadDashboard() {
    try {
      const [stats, topEarners, timeSpenders] = await Promise.all([
        this.api.getStatistics(),
        this.api.getRanking('HIGHER_EARNINGS'),
        this.api.getRanking('HIGHER_MINUTES')
      ]);

      this.updateUI({
        totalUsers: stats.totalKings,
        totalEarnings: stats.totalYearlyEarnings,
        averageTime: stats.totalYearlyMinutesSpent / stats.totalKings,
        topEarners,
        timeSpenders
      });

    } catch (error) {
      this.handleError(error);
    }
  }

  startAutoRefresh() {
    setInterval(() => this.loadDashboard(), this.refreshInterval);
  }
}
```

### 2. Sistema de Gamificação
```python
class BathroomGameSystem:
    def __init__(self, api_client):
        self.api = api_client
        
    def process_user_session(self, user_data):
        """Processa uma sessão de usuário e atualiza rankings"""
        try:
            # Calcular ganhos da sessão
            earnings = self.api.calculate_earnings(user_data)
            
            # Verificar conquistas
            achievements = self.check_achievements(earnings)
            
            # Atualizar ranking pessoal
            current_ranking = self.get_user_ranking_position(user_data.username)
            
            return {
                'earnings': earnings,
                'achievements': achievements,
                'ranking_position': current_ranking,
                'next_milestone': self.calculate_next_milestone(earnings)
            }
            
        except Exception as e:
            self.log_error(f"Erro processando sessão: {e}")
            return None
    
    def check_achievements(self, earnings):
        """Verifica conquistas baseadas nos ganhos"""
        achievements = []
        
        if earnings['dailyEarnings'] > 100:
            achievements.append('💰 High Roller')
            
        if earnings['dailyPercentageOfShift'] < 2:
            achievements.append('⚡ Speed Demon')
            
        return achievements
```

### 3. Relatórios Automatizados
```java
@Service
@Slf4j
public class ReportService {
    
    private final PaidThroneApiClient apiClient;
    private final EmailService emailService;
    
    @Scheduled(cron = "0 0 8 * * MON") // Toda segunda às 8h
    public void generateWeeklyReport() {
        try {
            log.info("Gerando relatório semanal");
            
            KingdomStatisticResponse stats = apiClient.getStatistics();
            List<RankingKingResponse> topEarners = apiClient.getRanking(RankingType.HIGHER_EARNINGS);
            
            String report = buildReport(stats, topEarners);
            
            emailService.sendReport("relatorio-semanal@empresa.com", "Relatório Semanal - Paid Throne", report);
            
            log.info("Relatório semanal enviado com sucesso");
            
        } catch (Exception e) {
            log.error("Erro ao gerar relatório semanal: {}", e.getMessage());
        }
    }
    
    private String buildReport(KingdomStatisticResponse stats, List<RankingKingResponse> topEarners) {
        StringBuilder report = new StringBuilder();
        
        report.append("📊 RELATÓRIO SEMANAL - PAID THRONE\n\n");
        report.append(String.format("👑 Total de usuários: %d\n", stats.totalKings()));
        report.append(String.format("💰 Ganhos totais anuais: R$ %,.2f\n", stats.totalYearlyEarnings()));
        report.append(String.format("⏰ Tempo total anual: %d minutos\n\n", stats.totalYearlyMinutesSpent()));
        
        report.append("🏆 TOP 5 MAIORES GANHOS:\n");
        for (int i = 0; i < topEarners.size(); i++) {
            RankingKingResponse user = topEarners.get(i);
            report.append(String.format("%d. %s - R$ %.2f/dia (%d min/dia)\n", 
                i + 1, user.username(), user.dailyEarnings(), user.dailyMinutesSpent()));
        }
        
        return report.toString();
    }
}
```

## 🚨 Tratamento de Erros

### Códigos de Erro e Tratamento
```typescript
class ApiErrorHandler {
  static handle(error: any): string {
    if (error.response) {
      switch (error.response.status) {
        case 400:
          return this.handleValidationError(error.response.data);
        case 500:
          return "Erro interno do servidor. Tente novamente em alguns minutos.";
        default:
          return `Erro HTTP ${error.response.status}: ${error.response.statusText}`;
      }
    } else if (error.request) {
      return "Erro de conexão. Verifique sua internet.";
    } else {
      return "Erro desconhecido. Contate o suporte.";
    }
  }

  private static handleValidationError(data: any): string {
    const errorMessages: Record<string, string> = {
      'username': 'Nome de usuário inválido',
      'averageBathroomTime': 'Tempo médio inválido (mínimo 5 minutos)',
      'numberOfVisitsPerDay': 'Número de visitas inválido (1-5 por dia)',
      'salary': 'Salário inválido (1 - 50.000 moedas)',
      'totalBathroomTime': 'Tempo total excede 60 minutos por dia'
    };

    return errorMessages[data.field] || data.message || 'Dados inválidos';
  }
}
```

### Retry e Circuit Breaker
```python
import time
from functools import wraps

class CircuitBreaker:
    def __init__(self, failure_threshold=5, recovery_timeout=60):
        self.failure_threshold = failure_threshold
        self.recovery_timeout = recovery_timeout
        self.failure_count = 0
        self.last_failure_time = None
        self.state = 'CLOSED'  # CLOSED, OPEN, HALF_OPEN
    
    def call(self, func):
        @wraps(func)
        def wrapper(*args, **kwargs):
            if self.state == 'OPEN':
                if time.time() - self.last_failure_time > self.recovery_timeout:
                    self.state = 'HALF_OPEN'
                else:
                    raise Exception("Circuit breaker is OPEN")
            
            try:
                result = func(*args, **kwargs)
                self.on_success()
                return result
            except Exception as e:
                self.on_failure()
                raise e
        
        return wrapper
    
    def on_success(self):
        self.failure_count = 0
        self.state = 'CLOSED'
    
    def on_failure(self):
        self.failure_count += 1
        self.last_failure_time = time.time()
        
        if self.failure_count >= self.failure_threshold:
            self.state = 'OPEN'

# Uso
circuit_breaker = CircuitBreaker()

@circuit_breaker.call
def safe_api_call(api_client, king):
    return api_client.calculate_earnings(king)
```

## ⚡ Boas Práticas

### 1. Cache de Resultados
```javascript
class CachedPaidThroneClient {
  constructor(apiClient, cacheTimeout = 300000) { // 5 minutos
    this.api = apiClient;
    this.cache = new Map();
    this.cacheTimeout = cacheTimeout;
  }

  async getRanking(type) {
    const cacheKey = `ranking_${type}`;
    const cached = this.cache.get(cacheKey);
    
    if (cached && Date.now() - cached.timestamp < this.cacheTimeout) {
      return cached.data;
    }

    const data = await this.api.getRanking(type);
    this.cache.set(cacheKey, {
      data,
      timestamp: Date.now()
    });

    return data;
  }

  async getStatistics() {
    const cacheKey = 'statistics';
    const cached = this.cache.get(cacheKey);
    
    if (cached && Date.now() - cached.timestamp < this.cacheTimeout) {
      return cached.data;
    }

    const data = await this.api.getStatistics();
    this.cache.set(cacheKey, {
      data,
      timestamp: Date.now()
    });

    return data;
  }
}
```

### 2. Validação Client-Side
```typescript
class KingValidator {
  static validate(king: King): string[] {
    const errors: string[] = [];

    // Username
    if (!king.username || king.username.length < 5 || king.username.length > 15) {
      errors.push('Username deve ter entre 5 e 15 caracteres');
    }
    if (!/^[a-zA-Z0-9]+$/.test(king.username)) {
      errors.push('Username deve conter apenas letras e números');
    }

    // Tempo
    if (king.averageBathroomTime < 5 || king.averageBathroomTime > 30) {
      errors.push('Tempo médio deve estar entre 5 e 30 minutos');
    }

    // Visitas
    if (king.numberOfVisitsPerDay < 1 || king.numberOfVisitsPerDay > 5) {
      errors.push('Número de visitas deve estar entre 1 e 5 por dia');
    }

    // Tempo total
    const totalTime = king.averageBathroomTime * king.numberOfVisitsPerDay;
    if (totalTime > 60) {
      errors.push('Tempo total não pode exceder 60 minutos por dia');
    }

    // Salário
    if (king.salary < 1 || king.salary > 50000) {
      errors.push('Salário deve estar entre R$ 1,00 e R$ 50.000,00');
    }

    return errors;
  }
}
```

### 3. Rate Limiting
```python
import time
from collections import defaultdict

class RateLimiter:
    def __init__(self, max_requests=100, window_seconds=3600):
        self.max_requests = max_requests
        self.window_seconds = window_seconds
        self.requests = defaultdict(list)
    
    def allow_request(self, user_id):
        now = time.time()
        user_requests = self.requests[user_id]
        
        # Remove requests antigas
        cutoff = now - self.window_seconds
        user_requests[:] = [req_time for req_time in user_requests if req_time > cutoff]
        
        if len(user_requests) >= self.max_requests:
            return False
            
        user_requests.append(now)
        return True

# Uso no cliente
rate_limiter = RateLimiter()

def safe_calculate_earnings(api_client, king, user_id):
    if not rate_limiter.allow_request(user_id):
        raise Exception("Rate limit exceeded")
    
    return api_client.calculate_earnings(king)
```

## 📊 Monitoramento

### Métricas Importantes
```javascript
class ApiMetrics {
  constructor() {
    this.metrics = {
      requests_total: 0,
      requests_success: 0,
      requests_error: 0,
      response_times: [],
      error_types: {}
    };
  }

  recordRequest(endpoint, duration, success, error = null) {
    this.metrics.requests_total++;
    
    if (success) {
      this.metrics.requests_success++;
    } else {
      this.metrics.requests_error++;
      
      if (error) {
        const errorType = error.response?.status || 'network_error';
        this.metrics.error_types[errorType] = (this.metrics.error_types[errorType] || 0) + 1;
      }
    }

    this.metrics.response_times.push(duration);
    
    // Manter apenas os últimos 100 tempos de resposta
    if (this.metrics.response_times.length > 100) {
      this.metrics.response_times.shift();
    }
  }

  getStats() {
    const responseTimes = this.metrics.response_times;
    const avgResponseTime = responseTimes.length > 0 
      ? responseTimes.reduce((a, b) => a + b, 0) / responseTimes.length 
      : 0;

    return {
      total_requests: this.metrics.requests_total,
      success_rate: this.metrics.requests_total > 0 
        ? (this.metrics.requests_success / this.metrics.requests_total) * 100 
        : 0,
      average_response_time: avgResponseTime,
      error_breakdown: this.metrics.error_types
    };
  }
}

// Integração com cliente
class MonitoredPaidThroneClient {
  constructor(apiClient) {
    this.api = apiClient;
    this.metrics = new ApiMetrics();
  }

  async calculate(king) {
    const startTime = Date.now();
    
    try {
      const result = await this.api.calculate(king);
      const duration = Date.now() - startTime;
      
      this.metrics.recordRequest('/calculate', duration, true);
      return result;
      
    } catch (error) {
      const duration = Date.now() - startTime;
      this.metrics.recordRequest('/calculate', duration, false, error);
      throw error;
    }
  }

  getMetrics() {
    return this.metrics.getStats();
  }
}
```

### Alertas
```python
class AlertSystem:
    def __init__(self, webhook_url=None):
        self.webhook_url = webhook_url
        self.thresholds = {
            'error_rate': 5.0,  # 5%
            'response_time': 2000,  # 2 segundos
            'availability': 95.0  # 95%
        }
    
    def check_and_alert(self, metrics):
        alerts = []
        
        if metrics['success_rate'] < self.thresholds['availability']:
            alerts.append(f"⚠️ Baixa disponibilidade: {metrics['success_rate']:.1f}%")
        
        error_rate = 100 - metrics['success_rate']
        if error_rate > self.thresholds['error_rate']:
            alerts.append(f"🚨 Alta taxa de erro: {error_rate:.1f}%")
        
        if metrics['average_response_time'] > self.thresholds['response_time']:
            alerts.append(f"⏰ Tempo de resposta alto: {metrics['average_response_time']:.0f}ms")
        
        if alerts:
            self.send_alerts(alerts)
    
    def send_alerts(self, alerts):
        message = "🔴 ALERTAS - Paid Throne API\n\n" + "\n".join(alerts)
        
        if self.webhook_url:
            # Enviar para Slack, Discord, etc.
            requests.post(self.webhook_url, json={"text": message})
        else:
            print(message)
```

---

## 🎯 Próximos Passos

1. **Implementar autenticação JWT** para segurança
2. **Adicionar cache Redis** para melhor performance
3. **Configurar monitoramento** com Prometheus/Grafana
4. **Implementar rate limiting** no lado servidor
5. **Criar SDK oficial** para linguagens populares

## 🤝 Suporte

Para dúvidas e suporte:
- 📧 Email: support@paidthrone.com
- 📚 Documentação: [API Docs](http://localhost:8080/calculator/swagger-ui/)
- 🐛 Issues: [GitHub Issues](https://github.com/JoaoMattheus/Paid-Throne/issues)

---

**Transforme momentos cotidianos em insights divertidos! 🚽💰**

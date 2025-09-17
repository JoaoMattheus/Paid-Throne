# 🚽 Trono Remunerado – Calculadora de Cagada Remunerada

O **Trono Remunerado** agora é um site PHP mobile first pensado para o domínio [teonoremunerado.com](https://teonoremunerado.com). A calculadora mostra quanto dinheiro você "ganha" durante o tempo passado no banheiro respeitando as regras oficiais do reino.

## ✨ Principais recursos

- **Mobile first**: interface leve e responsiva que funciona muito bem em smartphones e tablets.
- **SEO pronto para produção**: meta tags completas, sitemap, robots, dados estruturados e conteúdo otimizado.
- **Google Adsense & GA4**: bastam variáveis de ambiente para habilitar anúncios e métricas.
- **Política de privacidade e contato**: páginas dedicadas para monetização e conformidade.
- **Regras de negócio originais**: validações de tempo, salário, escalas de trabalho e cálculos diários/mensais/anuais.

## 🧮 Como o cálculo funciona

1. O usuário informa:
   - Nome (5 a 15 caracteres alfanuméricos).
   - Tempo médio por visita ao banheiro (mínimo 5 minutos).
   - Quantidade de visitas por dia (entre 1 e 5).
   - Salário e tipo de salário (hora, dia, mês ou ano).
   - Escala de trabalho (6x1, 5x2, 4x3 ou 12x36).
2. A aplicação valida as entradas, garantindo que o tempo total diário no trono não ultrapasse 60 minutos.
3. O **SalaryCalculator** converte o salário informado para valor por minuto e calcula os ganhos no banheiro.
4. O **BathroomCalculator** retorna a porcentagem do turno gasto no trono.
5. Os resultados exibem projeções diárias, mensais e anuais de tempo e dinheiro, além do impacto no expediente.

## ⚙️ Configuração

### 1. Clonar e instalar dependências

```bash
git clone https://github.com/sua-conta/teonoremunerado.git
cd teonoremunerado
cp .env.example .env
```

> A aplicação não depende de Composer – basta configurar as variáveis e apontar o servidor web para a pasta `public/`.

### 2. Variáveis de ambiente

| Variável | Descrição |
| --- | --- |
| `APP_NAME` | Nome exibido no site e metadados. |
| `APP_URL` | URL canônica (use HTTPS em produção). |
| `APP_ENV` | Ambiente atual (`production`, `staging`, `local`). |
| `APP_TIMEZONE` | Timezone padrão (ex.: `America/Sao_Paulo`). |
| `ADSENSE_CLIENT_ID` | ID do publisher do Google Adsense. |
| `ADSENSE_CALCULATOR_SLOT_ID` | Slot para o bloco de anúncio da calculadora. |
| `ANALYTICS_ID` | ID do GA4 (opcional). |
| `CONTACT_RECIPIENT_EMAIL` | Destinatário do formulário de contato. |
| `CACHE_TTL` | TTL sugerido para conteúdo estático (em segundos). |

### 3. Servindo localmente

#### Usando PHP embutido

```bash
php -S localhost:8000 -t public
```
Acesse `http://localhost:8000`.

#### Usando Docker

```bash
docker run --rm -it \
  -p 8080:80 \
  -v "$(pwd)":/var/www/html \
  -e APP_NAME="Trono Remunerado" \
  -e APP_URL="http://localhost:8080" \
  php:8.2-apache
```

> Para produção, utilize uma imagem própria (veja a seção **Deploy** abaixo).

#### Usando Docker Compose

```bash
docker compose up --build
```

O site ficará disponível em `http://localhost:8080` com volume persistente para logs.

## 📄 Estrutura de diretórios

```
├── app/                # Domínio, serviços e validações
├── public/             # Document root com páginas e assets
│   ├── assets/
│   │   ├── css/
│   │   ├── images/
│   │   └── js/
│   ├── contato.php
│   ├── index.php
│   ├── privacidade.php
│   ├── robots.txt
│   └── sitemap.xml
├── storage/logs/       # Armazena fallback do formulário de contato
├── templates/partials/ # Cabeçalho e rodapé compartilhados
├── bootstrap.php       # Autoloader e helpers
└── .env.example
```

## 🚀 Deploy sugerido

1. Crie uma imagem baseada em `php:8.2-apache`.
2. Copie o conteúdo do repositório para `/var/www/html`.
3. Habilite `mod_rewrite` se desejar URLs amigáveis (`a2enmod rewrite`).
4. Configure as variáveis de ambiente no provedor de hospedagem.
5. Ative cache para assets estáticos (CSS/JS/Imagens) com base no `CACHE_TTL`.
6. Aponte o domínio `teonoremunerado.com` para o servidor e configure HTTPS.

## 🧪 Lighthouse & Boas práticas

- Layout responsivo com contraste elevado e navegação por teclado.
- HTML semântico (`<header>`, `<main>`, `<section>`, `<article>`, `<details>`).
- Assets minificados e carregados com `preconnect`, `defer` e `async` quando necessário.
- Metadados completos para SEO (Open Graph, Twitter Cards, JSON-LD).

## 📬 Suporte e contato

Use a página [Contato](https://teonoremunerado.com/contato) ou envie um e-mail para `contato@teonoremunerado.com`.

Feito com humor real e muito cuidado com a experiência mobile. 💩

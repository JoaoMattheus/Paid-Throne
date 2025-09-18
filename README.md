# Trono Remunerado

Calculadora bem-humorada que estima quanto tempo e quanto dinheiro o trabalhador "ganha" no trono, respeitando a escala de trabalho. Projeto em PHP 8.2 + MySQL, mobile-first e com foco em SEO.

## Requisitos
- PHP 8.2+
- MySQL 8+
- Extensões PHP: `pdo_mysql`, `intl`

## Configuração
1. Copie o arquivo `.env.example` (crie o seu) com as variáveis:
   ```bash
   DB_HOST=127.0.0.1
   DB_PORT=3306
   DB_DATABASE=trono
   DB_USERNAME=usuario
   DB_PASSWORD=senha
   IP_HASH_SALT=salto-secreto
   ```
2. Execute a migration `database/migrations/001_create_submissions.sql` no banco.
3. Configure o servidor web para apontar para o diretório `public/`.

## Testes
Os testes de domínio utilizam PHPUnit (`tests/Domain/CalculatorTest.php`). Instale as dependências com Composer e execute `vendor/bin/phpunit`. Em ambientes sem acesso à internet, instale as dependências localmente antes do deploy.

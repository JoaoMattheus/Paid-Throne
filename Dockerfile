FROM php:8.2-apache

# Habilita módulos necessários
RUN a2enmod rewrite headers expires

# Copia arquivos para o container
COPY public/ /var/www/html/
COPY app/ /var/www/html/app/
COPY templates/ /var/www/html/templates/
COPY bootstrap.php /var/www/html/
COPY storage/ /var/www/html/storage/
COPY .env.example /var/www/html/

# Ajusta permissões para logs
RUN chown -R www-data:www-data /var/www/html/storage

# Define diretório de trabalho
WORKDIR /var/www/html

# Configura diretivas básicas para cache
RUN printf '<Directory /var/www/html>\n    AllowOverride All\n    Require all granted\n</Directory>\n' > /etc/apache2/conf-available/trono.conf \
    && a2enconf trono

ENV APP_NAME="Trono Remunerado" \
    APP_URL="http://localhost" \
    APP_ENV="production" \
    APP_TIMEZONE="America/Sao_Paulo"

EXPOSE 80

CMD ["apache2-foreground"]

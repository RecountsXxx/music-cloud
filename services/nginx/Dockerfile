FROM nginx

RUN apt-get update && apt-get -y upgrade && \
    apt-get install -y mc

WORKDIR /var/www/html

#COPY ../frontend /var/www/html

# Экспонируем порт Nginx (по умолчанию 80)
EXPOSE 80

# Команда запуска Nginx при старте контейнера
CMD ["nginx", "-g", "daemon off;"]
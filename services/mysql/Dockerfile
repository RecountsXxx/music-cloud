## Используем официальный образ MySQL в качестве базового
#FROM mysql:8.0
## Копируем скрипты инициализации базы данных (если есть) в контейнер
## COPY ./init.sql /docker-entrypoint-initdb.d/
#
## Указываем рабочую директорию
#WORKDIR /var/lib/mysql
#
## Определяем переменные окружения (если необходимо)
## ENV MYSQL_ROOT_PASSWORD=my-secret-pw
#
## Открываем порт 3306 для доступа к базе данных
#EXPOSE 3306

FROM mysql/mysql-server:latest
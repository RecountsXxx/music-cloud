## Используем официальный образ Ubuntu в качестве базового
#FROM ubuntu:20.04
#
## Устанавливаем Node.js и необходимые зависимости
#RUN apt-get update && apt-get install -y \
#    curl \
#    gnupg \
#    && curl -fsSL https://deb.nodesource.com/setup_14.x | bash - \
#    && apt-get install -y nodejs \
#    && apt-get clean

# Используем официальный образ Ubuntu в качестве базового
FROM ubuntu:20.04

# Устанавливаем зависимости для Node.js
RUN apt-get update && apt-get install -y \
    curl \
    gnupg \
    && curl -fsSL https://deb.nodesource.com/setup_14.x | bash - \
    && apt-get install -y nodejs \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

RUN npm init -y
RUN npm install express passport passport-oauth2 oauth2orize sequelize mysql2 dotenv

# Создаем рабочую директорию в контейнере
WORKDIR /app

# Копируем package.json и package-lock.json в рабочую директорию
COPY package*.json ./

# Устанавливаем зависимости
RUN npm install

# Копируем остальные файлы приложения в рабочую директорию
#COPY . .

# Указываем порт, который будет использоваться приложением
EXPOSE 3000

# Определяем команду запуска приложения
CMD ["node", "app.js"]

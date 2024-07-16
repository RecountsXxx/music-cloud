# Используем официальный образ Node.js в качестве базового
FROM node:14

# Создаем рабочую директорию в контейнере
WORKDIR /app

# Копируем package.json и package-lock.json в рабочую директорию
#COPY package*.json ./

# Устанавливаем зависимости
RUN npm init -y
RUN npm install

#RUN npm install express mysql knex
#RUN npm install --save-dev knex


# Копируем оставшиеся файлы в рабочую директорию
#COPY ../../services/nodejs .

# Указываем порт, который будет использоваться приложением
EXPOSE 3000

# Определяем команду запуска приложения
CMD ["node", "app.js"]
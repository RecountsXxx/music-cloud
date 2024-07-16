// db.js
const knex = require('knex');

const db = knex({
    client: 'mysql',
    connection: {
        host: 'mysql', // имя сервиса в docker-compose
        user: 'Renzo', // ваше имя пользователя
        password: '12345', // ваш пароль
        database: 'VibeCloud', // имя вашей базы данных
    },
});

module.exports = db;

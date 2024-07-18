require('dotenv').config();
const express = require('express');
const passport = require('passport');
const session = require('express-session');
const authRoutes = require('./routes/auth');
const sequelize = require('./models/index');
require('./config/passport');

const app = express();

app.use(express.json());
app.use(session({ secret: process.env.SESSION_SECRET, resave: false, saveUninitialized: false }));
app.use(passport.initialize());
app.use(passport.session());

app.use('/auth', authRoutes);

const PORT = process.env.PORT || 3000;

sequelize.sync().then(() => {
    app.listen(PORT, () => {
        console.log(`Server is running on port ${PORT}`);
    });
});

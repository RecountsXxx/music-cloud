const passport = require('passport');
const GoogleStrategy = require('passport-google-oauth20').Strategy;
const User = require('../models/user');

passport.use(new GoogleStrategy({
        clientID: process.env.GOOGLE_CLIENT_ID,
        clientSecret: process.env.GOOGLE_CLIENT_SECRET,
        callbackURL: '/auth/google/callback'
    },
    async (token, tokenSecret, profile, done) => {
        try {
            let user = await User.findOne({ where: { email: profile.emails[0].value } });
            if (!user) {
                user = await User.create({ email: profile.emails[0].value, password: '' });
            }
            done(null, user);
        } catch (err) {
            done(err, null);
        }
    }));

passport.serializeUser((user, done) => {
    done(null, user.id);
});

passport.deserializeUser(async (id, done) => {
    try {
        const user = await User.findByPk(id);
        done(null, user);
    } catch (err) {
        done(err, null);
    }
});

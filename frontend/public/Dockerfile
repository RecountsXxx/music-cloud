FROM node:lts

WORKDIR /frontend/public/app

COPY --chown=node:node ./src/package*.json ./

RUN npm install

COPY --chown=node:node . .

EXPOSE 5173

CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0"]
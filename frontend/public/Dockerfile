FROM node:20-alpine

WORKDIR /frontend/public/app

COPY --chown=node:node ./src/package*.json ./

RUN npm install

COPY --chown=node:node . .

EXPOSE 7000

CMD ["npm", "run", "start"]
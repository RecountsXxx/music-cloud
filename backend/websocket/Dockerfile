FROM node:20-alpine

WORKDIR /usr/src/app

COPY --chown=node:node ./src/package*.json ./
COPY --chown=node:node ./src/tsconfig*.json ./

RUN npm ci

COPY --chown=node:node . .

RUN npm run build

ENTRYPOINT ["/bin/sh", "-c", "npm run start:dev"]
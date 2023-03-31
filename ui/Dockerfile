# Dockerfile
FROM node:16.15-alpine AS base
WORKDIR /app
RUN apk update && apk upgrade
RUN apk add --no-cache python3 make g++


COPY package.json yarn.lock ./
RUN yarn install

COPY . .

ENV NUXT_HOST=0.0.0.0
ENV NUXT_PORT=3000
EXPOSE 3000

FROM base AS test
RUN yarn lint
CMD [ "yarn", "test" ]

FROM base AS dev
CMD [ "yarn", "dev" ]

FROM base AS prod
RUN yarn run build
CMD [ "yarn", "start" ]

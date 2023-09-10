# Set up
FROM alpine:3.18 as base

ARG PORT=3000
ENV NODE_ENV=production

ENV YARN_VERSION 1.22.19

ARG URL=http://moodle-grades-master-backend:8090
ENV REWRITE_URL=$URL

ADD . /app
WORKDIR /app

RUN apk update && apk add nodejs npm \
    && npm install -g yarn \
    && yarn policies set-version $YARN_VERSION \
    && yarn add nuxt \ 
    && yarn install

# Build
FROM base as build

RUN yarn build

# Run
FROM base

ENV PORT=$PORT

COPY --from=build /app/.output /app/.output

CMD [ "node", ".output/server/index.mjs" ]
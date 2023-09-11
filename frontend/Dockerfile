# Set up
FROM alpine:3.18

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
RUN yarn build

# Run
ENV PORT=$PORT

COPY /app/.output /app/.output

CMD [ "node", ".output/server/index.mjs" ]
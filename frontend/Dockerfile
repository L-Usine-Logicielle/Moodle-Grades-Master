# Set up
FROM node:alpine3.17 as base

ARG PORT=3000
#ENV NODE_ENV=production

ENV YARN_VERSION 3.4.1

ARG URL=http://moodle-grades-master-backend:8090
ENV REWRITE_URL=$URL

ADD . /app
WORKDIR /app

RUN yarn policies set-version $YARN_VERSION \
    && yarn install

# Build
FROM base as build

RUN yarn build

# Run
FROM base

ENV PORT=$PORT

COPY --from=build /app/.output /app/.output

CMD [ "node", ".output/server/index.mjs" ]
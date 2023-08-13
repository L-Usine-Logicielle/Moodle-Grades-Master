# Set up
FROM node:alpine3.17 as base

ARG PORT=3000
ENV NODE_ENV=production

ENV YARN_VERSION 3.4.1

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
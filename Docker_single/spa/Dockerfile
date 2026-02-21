FROM node:25-alpine as build

RUN mkdir -p /spa
COPY ./spa/package.json /spa/package.json
COPY ./spa/package-lock.json /spa/package-lock.json
WORKDIR /spa
RUN npm ci

COPY ./spa ./

RUN npm run build

FROM nginx:stable-alpine3.23-perl
RUN mkdir -p /static
RUN rm /etc/nginx/conf.d/default.conf
COPY ./Docker_single/spa/proxy.conf /etc/nginx/nginx.conf
COPY --from=build /spa/dist/ /static/
CMD ["nginx", "-g", "daemon off;"]
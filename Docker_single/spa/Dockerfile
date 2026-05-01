FROM node:25-alpine as build

RUN mkdir -p /spa
COPY ./spa/package.json /spa/package.json
COPY ./spa/package-lock.json /spa/package-lock.json
WORKDIR /spa
RUN npm ci

ARG VITE_API_URL
ENV VITE_API_URL=$VITE_API_URL
RUN echo $VITE_API_URL

ENV VITE_API_URL=http://jeff217.mikrus.xyz:20217

COPY ./spa ./

RUN npm run build

FROM nginx:stable-alpine3.23-perl
RUN mkdir -p /static
RUN rm /etc/nginx/conf.d/default.conf
COPY ./Docker_single/spa/nginx.conf /etc/nginx/nginx.conf
COPY ./Docker_single/spa/0.0.1-prod.conf /etc/nginx/conf.d/0.0.1-prod.conf
COPY --from=build /spa/dist/ /static/
CMD ["nginx", "-g", "daemon off;"]
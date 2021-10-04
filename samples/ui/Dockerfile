FROM node:latest as builder
WORKDIR /var/www/app

COPY ./package*.json ./
RUN npm install -g tsc
RUN npm install
COPY . ./
RUN npm run build

FROM nginx:alpine
WORKDIR /usr/share/nginx/html
COPY --from=builder /var/www/app/dist ./
COPY webserver/nginx.conf /etc/nginx/
RUN rm /etc/nginx/conf.d/default.conf
COPY webserver/default.conf /etc/nginx/conf.d/default.conf

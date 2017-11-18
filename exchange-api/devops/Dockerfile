FROM node:8.9.1

RUN wget https://github.com/Yelp/dumb-init/releases/download/v1.2.0/dumb-init_1.2.0_amd64.deb \
  && dpkg -i dumb-init_*.deb \
  && apt-get update \
  && apt-get -y install apt-transport-https \
  && curl -sS https://dl.yarnpkg.com/debian/pubkey.gpg | apt-key add - \
  && echo "deb https://dl.yarnpkg.com/debian/ stable main" | tee /etc/apt/sources.list.d/yarn.list \
  && apt-get update \
  && apt-get -y install yarn

EXPOSE 3210
ARG MYSQL_URL=localhost
ENV MYSQL_URL=$MYSQL_URL
ARG MYSQL_USER=root
ENV MYSQL_USER=$MYSQL_USER
ARG MYSQL_PASSWORD
ENV MYSQL_PASSWORD=$MYSQL_PASSWORD
ARG MYSQL_DATABASE=elmma
ENV MYSQL_DATABASE=$MYSQL_DATABASE
ARG MYSQL_TABLE=dax
ENV MYSQL_TABLE=$MYSQL_TABLE

WORKDIR /usr/app
ADD . /usr/app
RUN yarn
# Runs "/usr/bin/dumb-init -- /my/script --with --args"
ENTRYPOINT ["/usr/bin/dumb-init", "--"]

CMD ["node", "src/app.js"]
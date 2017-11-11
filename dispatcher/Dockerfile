# following https://nodejs.org/en/docs/guides/nodejs-docker-webapp/
FROM node:boron

# The Nodejs dispatcher will run at port:
EXPOSE 4242

# Create app directory
WORKDIR /usr/src/app

# Install app dependencies
COPY package.json .
# For npm@5 or later, copy package-lock.json as well
# COPY package.json package-lock.json ./

RUN npm install
# If you are building your code for production
# RUN npm install --only=production

# Bundle app source
COPY . .


CMD [ "npm", "start" ]
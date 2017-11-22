# exchange-api
The exchange api gets live values from a DAX websites and writes them to an external
store so that other services can access it.

## Endpoints
The scraper will start scraping immediately in an always-on mode. Use `/pause` to pause it
and `/unpause` to bring it back to live.

## Running locally
Check the values in config.js and adjust your env vars accordingly. Then simply run:

    node src/app.js

## Running in docker
If you want to run the server locally you should use docker. Simply bBuild the container:

    docker build -t exchangeapi:latest -f devops/Dockerfile .

The launch it:

    docker run -e MYSQL_URL=10.0.75.1 -p 3210:3210 -it exchangeapi:latest

Assuming you have mysql running on localhost, obtain your Docker NAT IP via ipconfig and pass
it as `MYSQL_URL`. This way the docker container can access the mysql instance running on the
host system.

Also make sure the user on your mysql instance has access permissions from everywhere, not
just from localhost.

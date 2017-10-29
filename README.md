# elmma

## Run all services

To run all services the docker-compose.yml file consists of, type `docker-compose up -d` (-d means running in daemon mode)

## Serve the Exchange Api

To build the docker exchange-api image, `docker build -t eimma-exchange-api -f eimma/exchange-api/Dockerfile .`

To run the image, type `docker run -it --rm --name exchange-api -d -v {$PWD}/exchange-api:/usr/src/app -p 8080:8080 eimma-exchange-api`

Exchange Api is accessible by http://localhost:8080, to display the historical DAX data for the duration of 2016-01-01 to 2017-01-01, call http://localhost:8080/?ticker=^GDAXI&from=2016-01-01&to=2017-01-01
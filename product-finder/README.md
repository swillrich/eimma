# product finder

a service that parses data from a product list page - like on onvista.de or finanztreff.de - to find the best knock out certificates for the current DAX price. 

## Running on localhost

    npm update
    start and call:
    http://localhost:3211/daxPrice=13.067,00&minDistance=100&type=long&openEnd=true
	
Then launch it:

	node src/app.js
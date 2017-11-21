const request = require('request');
//const exporter = require('./exporter');
//const logger = require('./util/logger');

exports.scrapeProducts = async (req, res) => {
    // Set the headers
    var headers = {
        'User-Agent':       'Super Agent/0.0.1',
        'Content-Type':     'application/x-www-form-urlencoded; charset=UTF-8'
    }

    // Configure the request
    var options = {
        url: 'https://www.onvista.de/derivative/ajax/getFinderResult',
        method: 'POST',
        headers: headers,
        form: {
            'derivativeType': 'Knock Outs', 
            'searchFields[idGroupIssuer][]': '53152',
            'searchFields[sort]': 'GEARING_ASK_DESC',
            'searchFields[idExerciseRight]': '2',
            'searchFields[blocksize]': '50'
        }
    }

    // Start the request
    var results = "";
    request(options, function (error, response, body) {
        if (!error && response.statusCode == 200) {
            // Print out the response body
            results = body;
            console.log(results);
            //logger.log(logger.INFO, `Onvista Knock Outs: ${results}`);
        }
    })
    
    //work with the results, find what we want
    //exporter.export(products, 'finanzen.net');
    var products = results; //at the moment
    res(products);
};
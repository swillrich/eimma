const axios = require('axios');
//const exporter = require('./exporter');
//const logger = require('./util/logger');

exports.scrapeProducts = async (req, res) => {
    
    // Configure the request with axios
    axios.post('https://www.onvista.de/derivative/ajax/getFinderResult', {
        derivativeType                  : 'Knock Outs', 
        'searchFields[idGroupIssuer][]' : '53152',
        'searchFields[sort]'            : 'GEARING_ASK_DESC',
        'searchFields[idExerciseRight]' : '2',
        'searchFields[blocksize]'       : '50'
    })
    .then(function (response) {
        //console.log(response);
        var products = response.results;
        //res("Produkte:"+response);
        var_dump(response);
        res(response);
    })
    .catch(function (error) {
        res(error);
    });
    
};
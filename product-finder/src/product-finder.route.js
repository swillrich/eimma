const axios = require('axios');
const logger = require('./util/logger');

exports.scrapeProducts = async (req, res) => {
    
    try {
        // Configure the request with axios
        const response = await axios.post('https://www.onvista.de/derivative/ajax/getFinderResult', {
            derivativeType                  : 'Knock Outs', 
            'searchFields[idGroupIssuer][]' : '53152',
            'searchFields[sort]'            : 'GEARING_ASK_DESC',
            'searchFields[idExerciseRight]' : '2',
            'searchFields[blocksize]'       : '50'
        });
        var products = response.data["results"];
        res(products);
    } catch (error) {
        res(error);
    }
};
const axios = require('axios');
const logger = require('./util/logger');

/**
 * scrapes all the necessary products from onvista.de
 * at the moment: Goldman Sachs Certificates
 */
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
        var products = minimizeProductResults(response.data["results"]);
        
        res(products);
    } catch (error) {
        res(error);
    }
};

/**
 * we have a lots of results. Go and minimize them to what we really want
 * @param products All the producst we had received from onvista as "result"-field in the response
 */
function minimizeProductResults(products) {

    var knockOuts = new Array();
    var maxCount = 3;
    var count = 0;
    for(var item of products) {
        count++;
        knockOuts.push(
            {
                wkn:item.nsin,
                isin:item.isin,
                productName:item.shortNameInstrument,
                emittent:item.nameIssuer,
                emittentID:item.idGroupIssuer,
                underlyingID:item.nsinUnderlying,
                underlyingName:item.shortNameUnderlying,
                //price: valueField1,
                //priceSL: valueField2,
            }
        );
        if (count > maxCount) {
            break;
        }
    };

    return knockOuts;

};
const axios = require('axios');
const cheerio = require('cheerio');
const logger = require('./util/logger');

/**
 * scrapes all the necessary products from onvista.de
 * you must set the following filters as GET-params, I'd like to describe:
 *  
 * daxPrice     = current DAX price, pass sth like '13.005,00' 
 * minDistance  = a minimum (DAX PKT) between price and strike (KO), pass sth like  "100"
 * type         = long (call) or short (put) certificates, pass "long" or "short"
 * openEnd      = only open end certificates? pass true or false
 * 
 * @example: http://[...]/daxPrice=13.067,00&minDistance=100&type=long&openEnd=true
 */
exports.scrapeProducts = async (req, res) => {
    try {
        
        var aDAXPrice           = Number(parseFloat(decodeURIComponent(req.params.daxPrice).replace('.','').replace(',','.'))); // pass '13.005,00', we convert '13.105,01' to '13105.01'
        var aMinDistance        = Number(decodeURIComponent(req.params.minDistance));   // pass in format 100 or 100.00 (for 100 PKT before Strike/K.O.)
        var aTypeCode           = (decodeURIComponent(req.params.type)=="long")?"RISE":"FALL"; //pass long or short
        var aBasiswert          = "DAX%20®";
        var anUnderlyingID      = 159096;
        var anEmittentID        = "16";
        var anEmittentShortName = "GS";
        var openEndOnly         = Boolean(decodeURIComponent(req.params.openEnd));  // pass true or false
        
        //validation for parameters:
        if (aMinDistance==NaN) //must be a valid Number format like 100 or 100.00
        	throw new Error("invalid number format for parameter minDistance. Must be 100 or 100.00");
        if (aDAXPrice==NaN) //must be a valid Number format like 13.005,00 or 13005
            throw new Error("invalid number format for parameter daxPrice. Must be e.g. '13.005,00'");

        const finanztreffProducts = await scrapeFinanztreff(
            aDAXPrice,aMinDistance,aTypeCode,aBasiswert,anEmittentID,anEmittentShortName,anUnderlyingID,openEndOnly);
        res(finanztreffProducts);
    } catch (error) {
        res(error);
    }    
};

/**
 * scrape table in finanztreff.de using the follwing filters/params:
 * 
 * aDAXPrice    = current DAX Price
 * aMinDistance = minimum distance between the strike value (K.O.) and the current underlying's price (DAX)
 * aTypeCode    = RISE (Long= or FALL (Short)
 * aBasiswert   = url safe name of the underlying like 'DAX%20®' (see hard values of the page)
 * anEmittentShortName = is the short name, like "GS" for Goldman Sachs (see table below)
 * anEmittentID = Emittent IDs, their anEmittentShortName and long titles:
 *   12 : BNP   = BNP Paribas, 
 *   9  : CIT   = CitiGroup, 
 *   10 : COB   = Commerzbank, 
 *   6  : DZ    = DZ Bank Ag, 
 *   16 : GS    = Goldman Sachs, 
 *   5  : HSBC  = HSBCTrinkaus & Burkhardt AG, 
 *   1  : DBK   = Deutsche Bank, 
 *   33 : MST   = Morgan Stenley, 
 *   3  : UBS   = UBS
 * anUnderlyingID = ID of the underlying (see hard values of the page)
 * openEndOnly  = true or false for only openEnd products (no maturity)
 */
async function scrapeFinanztreff(aDAXPrice,aMinDistance,aTypeCode,aBasiswert,anEmittentID,
    anEmittentShortName,anUnderlyingID,openEndOnly){
    //build a url for finanztreff
    const url = 'http://knock-outs.finanztreff.de/dvt_suche.htn'+
    '?ansicht=basis'+'&basiswert='+aBasiswert+'&basiswertHidden='+aBasiswert+
    '&deltaStopLoss=0'+'&eqStopLoss=3'+'&gearing=0'+'&iidp='+anEmittentID+
    '&indexFactorUnderlyingGroupId='+'&kategorie='+'&maturityDateMax='+
    '&maturityDateMin='+'&quanto='+'&radio_knockoutkennzahl=0'+
    '&securityCategoryCodeUnderlying='+'&securityGroupCode='+'&securityTypeCode='+
    '&seite=turbos'+'&selectPartner=0'+'&strikeMax=&strikeMin='+'&suche=1'+
    '&typeCode='+aTypeCode+
    '&underlying='+anUnderlyingID;

    //get html using axios and the jQuery data using cheerio
    const html = await axios(url);    
    const $ = cheerio.load(html.data);
    var product = {};

    //use jQuery and iterate through the table's tr and tds
    $(".extendetKursliste").find('tr').each(function (i, element) {
        var $tds = $(this).find('td');
        //we need some properties, that we can validate for a complete and useful dataset (no advertising or something else)
        const valType           = $tds.eq(2).text().trim();
        const valEmittent       = $tds.eq(11).text().trim();
        const valExtracharge    = $tds.eq(9).text().trim(); //this should be positive ;) no "-" at the beginning
        const valAutostoploss   = Number($tds.eq(6).text().replace('PKT','').replace('.','').replace(',','.').trim()); //'13.105,01 PKT' => '13105.01'
        const valMaturity       = $tds.eq(10).text().trim(); //'24.11.17' or 'Open End'	

        //Check validation rules for a valid return set
        if ((valType+valEmittent).length != 0                       //no th stuff or blank lines here
            && valExtracharge.charAt(0) != "-"                      //no "-5,00%" stuff, we only want positive things (obviously a bug)
            && valEmittent==anEmittentShortName                     //check against the short name (we have some advertise offers ;)
            && (openEndOnly===true?(valMaturity=="Open End"):true)
            && (aTypeCode=="RISE"?(aDAXPrice - aMinDistance >= valAutostoploss): true) //check against the distance (13000 - 100 = 12900 >= 12500SL for LONG examples)
            && (aTypeCode=="FALL"?(aDAXPrice + aMinDistance <= valAutostoploss): true) //check against the distance (13000 + 100 = 13100 <= 13200SL for SHORT examples)
        ){
            product = {
                wkn             : $tds.eq(1).text().trim(),
                type            : valType,
                bis             : $tds.eq(3).text().trim(),
                ask             : $tds.eq(4).text().trim(),
                strike          : $tds.eq(5).text().trim(),
                autostoploss    : valAutostoploss,
                leverage        : $tds.eq(7).text().trim(),
                diffSL          : $tds.eq(8).text().trim(),
                extracharge     : valExtracharge,
                maturity        : valMaturity,
                emittent        : valEmittent               
            };
            //stop here and return this first match
            return false;            
        }
    });
    return product;
};
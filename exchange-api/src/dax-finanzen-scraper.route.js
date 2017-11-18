const axios = require('axios');
const cheerio = require('cheerio');
const exporter = require('./exporter');
const logger = require('./util/logger');

const DAX_PRICE_SELECTOR =
  'body > div.wrapper > div.container.mobile > div.shadow > ' +
  'div:nth-child(13) > div:nth-child(1) > div > div > ' +
  'div.row.snapshot-headline > div.col-sm-7 > div.row.quotebox ' +
  '> div.col-xs-5.col-sm-4.text-sm-right.text-nowrap';

exports.scrapeFinanzenNetDax = async (req, res) => {
  const startTime = new Date();
  const html = await axios('http://www.finanzen.net/index/DAX-Realtime');
  const $ = cheerio.load(html.data);
  const priceString = $(DAX_PRICE_SELECTOR).text().slice(0, -3);
  const price = parseFloat(priceString);
  exporter.export(price, 'finanzen.net');
  logger.log(logger.INFO, `Current DAX price: ${price}`);
  logger.log(logger.INFO, `Execution Time: ${Math.abs(new Date() - startTime) / 1000}`);
  res(null);
};

const logger = require('./util/logger');
const exporter = require('./exporter');
const finanzenNetScraper = require('./scrapers/daxFinanzenNet.scraper');

let interval;
let lastDax = 0;

exports.startScraping = () => {
  interval = setInterval(scrape, 5000);
};

exports.pauseScraping = (req, res) => {
  clearInterval(interval);
  res(null);
};

exports.unpauseScraping = (req, res) => {
  exports.startScraping();
  res(null);
};

/**
 * Interval callback function.
 */
const scrape = async () => {
  const dax = await finanzenNetScraper.scrape();
  // We don't want to spam the DB.
  if (dax != lastDax) {
    logger.log(logger.INFO, 'New dax price found, exporting it.');
    exporter.export(dax, 'finanzen.net');
    lastDax = dax;
  }
};

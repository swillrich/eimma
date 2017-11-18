const Hapi = require('hapi');
const daxRouter = require('./dax-finanzen-scraper.route');
const exporter = require('./exporter');
const logger = require('./util/logger');

// Make sure the exporter connected prior to starting up the server.
exporter.connect();

const server = new Hapi.Server({
  debug: {request: ['error']},
  connections: {
    router: {
      stripTrailingSlash: true,
    },
  },
});

server.connection({port: 3210, host: '0.0.0.0'});
server.route({
    method: 'GET',
    path: '/',
    handler: daxRouter.scrapeFinanzenNetDax,
});

server.route({
    method: 'GET',
    path: '/_system/health',
    handler: (req, res) => res(null),
});

server.start((err) => {
    if (err) {
        throw err;
    }
    logger.log(logger.INFO, `Scraper started @3210 (${server.info.uri})`);
});

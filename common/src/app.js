const Hapi = require("hapi");
const libAccess = require("./libAccess");
const logger = require("./util/logger");

const server = new Hapi.Server({
  debug: { request: ["error"] },
  connections: {
    router: {
      stripTrailingSlash: true
    }
  }
});

server.connection({ port: 3220, host: "0.0.0.0" });
server.route({
  method: "GET",
  path: "/test",
  handler: libAccess.hello
});

server.route({
  method: "GET",
  path: "/_system/health",
  handler: (req, res) => res(null)
});

server.start(err => {
  if (err) {
    throw err;
  }
  logger.log(logger.INFO, `elmma-common started @3210 (${server.info.uri})`);
});

const logger = require("./util/logger");
const pB = require("./common/product-builder");

exports.hello = (req, res) => {
  res(pB.buildKO("wkn ..."));
};
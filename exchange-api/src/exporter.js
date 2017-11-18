const mysql = require('mysql');
const config = require('./config');
const logger = require('./util/logger');

let connection;

/**
 * Acts as a clean interface for the rest of the application. Can
 * be later on replaced with a connection to any queue system or
 * dupe writes.
 * @param {double} value The current dax value.
 * @param {string} source The data source.
 */
exports.export = (value, source) => {
  if (!connection) {
    logger.log(logger.ERROR, 'No SQL connection');
    return;
  }
  const timestamp = new Date().toISOString().slice(0, 19).replace('T', ' ');
  const sql =
    `INSERT INTO ${config.MYSQL_DAX_TABLE}` +
    `(timestamp, value, source) VALUES ('${timestamp}', ${value}, '${source}')`;
  // @todo: Use a promise-based lib to escape the callback hell.
  connection.query(sql, (err, result) => {
    if (err) {
      logger.log(logger.ERROR, err);
    }
  });
};

exports.connect = async () => {
  connection = mysql.createConnection({
    host: config.MYSQL_URL,
    user: config.MYSQL_USER,
    password: config.MYSQL_PASSWORD,
    database: config.MYSQL_DATABASE,
  });

  await connection.connect();
};

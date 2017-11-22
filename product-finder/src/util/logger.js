const config = require('../config');

exports.DEBUG = 'DEBUG';
exports.INFO = 'INFO';
exports.ERROR = 'ERROR';

/**
 * Logs a message to stdout. Is deactivated while testing.
 * @param {string} logLevel One of the class's log level constants
 * @param {string} message The log message
 * @param {string} env The environment, defaults to the global config
 * @param {Object} out The desired output stream, console by default.
 */
exports.log = (logLevel, message, env = config.ENVIRONMENT, out = console) => {
  const payload = {
    log_level_s: logLevel,
    message_t: message,
  };
  const formatted = JSON.stringify(payload);
  // Don't send logs while we're testing
  if (env === 'test') return;
  switch (logLevel) {
    case exports.DEBUG:
      out.debug(formatted);
      break;
    case exports.INFO:
      out.info(formatted);
      break;
    case exports.ERROR:
      out.error(formatted);
      // Fix to prevent circular logging.
      /* if (!message.includes('Metrics')) {
        metrics.trackError();
      } */
      break;
  }
};
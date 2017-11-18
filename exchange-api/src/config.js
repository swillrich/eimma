module.exports = {
  MYSQL_URL: process.env.MYSQL_URL || 'localhost',
  MYSQL_USER: process.env.MYSQL_USER || 'root',
  MYSQL_PASSWORD: process.env.MYSQL_PASSWORD || '',
  MYSQL_DATABASE: process.env.MYSQL_DATABASE || 'elmma',
  MYSQL_DAX_TABLE: process.env.MYSQL_DAX_TABLE || 'dax',
  ENVIRONMENT: process.env.NODE_ENV || 'production',
};

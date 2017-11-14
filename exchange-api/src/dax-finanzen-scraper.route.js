const puppeteer = require('puppeteer');

const DAX_PRICE_SELECTOR = 'body > div.wrapper > div.container.mobile > div.shadow > div:nth-child(13) > div:nth-child(1) > div > div > div.row.snapshot-headline > div.col-sm-7 > div.row.quotebox > div.col-xs-5.col-sm-4.text-sm-right.text-nowrap';

exports.scrapeFinanzenNetDax = async (req, res) => {
  const BROWSER = await puppeteer.launch();
  const page = await BROWSER.newPage();
  await page.goto('http://www.finanzen.net/index/DAX-Realtime');
  const bodyHandle = await page.$(DAX_PRICE_SELECTOR);
  const html = await page.evaluate(body => body.innerHTML, bodyHandle);
  await bodyHandle.dispose();
  res(html);
}
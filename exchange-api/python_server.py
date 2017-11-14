#!/usr/bin/env python
 
from http.server import BaseHTTPRequestHandler, HTTPServer
from yahoo_historical import Fetcher
from urllib.parse import urlparse, parse_qs
import jsonpickle
from yahoo_finance import Share
import traceback
from lxml import html
import requests
import time
from bs4 import BeautifulSoup
import urllib.request
from selenium import webdriver
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities

#tickerFile = open("ticker.txt")
#tickerList = tickerFile.read()
#tickerArr = tickerList.split("\n")
#BASE_URL = "https://www.google.com/finance?q=NASDAQ%3A"


counter = 0
driver = None

def connect():
    global driver
    driver = webdriver.Remote("http://headless:4444/wd/hub", DesiredCapabilities.CHROME)
    driver.get("http://www.finanzen.net/index/DAX-Realtime")

def getRealTimePrices():
    global counter
    global driver
    if counter == 20:
        counter = 0
        connect()
    counter = counter + 1
    pkt = driver.find_elements_by_css_selector('span.push-data')[12].text
    time = driver.find_elements_by_css_selector('span.push-data')[13].text
    return '{"price" : "'+pkt+'","time" : "'+time+'"}' 

def get_stock_price():
    """
    Scrapes google finance and the ticket symbol
    page and returns stock price for each symbol
    """
    for ticker in tickerArr:
        url = "https://finance.google.com/finance?q=INDEXDB:DAX"
        if ticker != "DAX":
            url = BASE_URL + ticker
        
        with urllib.request.urlopen(url) as url:
            htmltext = url.read()

        soup = BeautifulSoup(htmltext, 'html.parser')
        
        market_data = soup.find('span', attrs={'class' : 'pr'})
        price_fluctuation = soup.find('span', attrs={'class' : 'chg'})
        stock_price = market_data.text.strip()
        
        time_market_data = soup.find('span', attrs={'id' : 'ref_14199910_ltt'})
        timeChange = time_market_data.text.strip()
        #if price_fluctuation is None:
        #    print ticker, ": $", stock_price
        #else:
        #    print ticker, ": $", stock_price, "Price Fluctuation: $", price_fluctuation.text.strip()
        return '{"price" : "'+stock_price+'","time" : "'+timeChange+'"}'
        

        
def extract_var(var, query):
  return str(query[var]).replace('[','').replace(']','').replace('\'','')

def extract_date(var, query):
  a = extract_var(var, query)
  aa = a.split('-')
  arr = [int(aa[0]),int(aa[1]),int(aa[2])]
  return arr
  
def returnValueAsString(tree, input):
    return (str(tree.xpath(input)) + str("\"")).replace("]","").replace("[","").replace("'","")
 
# HTTPRequestHandler class
class testHTTPServer_RequestHandler(BaseHTTPRequestHandler):
 
  # GET
  def do_GET(self):
        # Send response status code
        self.send_response(200)
 
        # Send headers
        self.send_header('Content-type','text/json')
        self.end_headers()
        
        try:
          self.wfile.write(getRealTimePrices().encode('utf-8'))

        except:
          self.wfile.write(traceback.format_exc().encode('utf-8'))
          
        return
 
def run():
  print('starting server...')
 
  # Server settings
  # Choose port 8080, for port 80, which is normally used for a http server, you need root access
  server_address = ('0.0.0.0', 8080)
  httpd = HTTPServer(server_address, testHTTPServer_RequestHandler)
  print('running server...')
  httpd.serve_forever()

while True:
    try:
        print("Trying to connect")
        connect()
        break
    except:
        print("server http://headless:4444/wd/hub yet down")
    time.sleep( 5 )

print("Headless server is running")
 
run()
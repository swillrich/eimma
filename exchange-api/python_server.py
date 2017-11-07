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
          query = parse_qs(urlparse(self.path).query)
        
          if "favicon.ico" in self.path:
            self.wfile.write("no variables given".encode('utf-8'))
            return
    
          if "history" in self.path:        
            #https://github.com/lukaszbanasiak/yahoo-finance
            ticker = extract_var("ticker", query)
            f = extract_date("from", query)
            to = extract_date("to", query)
            # Send message back to client
            #data = Fetcher('^GDAXI', [2016,1,1], [2017,1,1])
            print("ticker is: " + ticker)
            print("from: " + repr(f))
            print("to: " + repr(to))
            data = Fetcher(ticker, f, to)
            d = data.getHistorical()
            d.to_json('/usr/src/data_fetched.csv')
            f = open('/usr/src/data_fetched.csv', 'r')
            # Write content as utf-8 data
            self.wfile.write(f.read().encode('utf-8'))
            f.close()
            return

          if "snapshot" in self.path:
            page = requests.get('http://www.xetra.com/xetra-de/')
            tree = html.fromstring(page.content)
            price = "{ \"price\":\"" + returnValueAsString(tree, '//*[@id="idmsChartLast"]/text()')
            timeS = ",\"time\":\"" + returnValueAsString(tree, '//*[@id="idmsChartTime"]/text()') + str(" }")
            dateS = ",\"date\":\"" + returnValueAsString(tree, '//*[@id="idmsChartDate"]/text()')
            
            #page = requests.get('http://www.ariva.de/db_dax_indikation-index/kurs')
            #tree = html.fromstring(page.content)
            #price = "{ \"price\":\"" + returnValueAsString(tree, '//*[@id="pageQuotes"]/div[2]/div[1]/table/tbody/tr[2]/td[2]/span/text()')
            #timeS = ",\"time\":\"" + returnValueAsString(tree, '//*[@id="pageQuotes"]/div[2]/div[1]/table/tbody/tr[2]/td[10]/span/text()') + str(" }")
            #dateS = time.strftime("%Y-%m-%d")

            self.wfile.write((str(price) + str(dateS) + str(timeS)).encode('utf-8'))

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
 
 
run()
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
 
#page = requests.get('http://www.xetra.com/xetra-de/')
#tree = html.fromstring(page.content)
#price = "{ \"price\":\"" + returnValueAsString(tree, '//*[@id="idmsChartLast"]/text()')
#timeS = ",\"time\":\"" + returnValueAsString(tree, '//*[@id="idmsChartTime"]/text()') + str(" }")
#dateS = ",\"date\":\"" + returnValueAsString(tree, '//*[@id="idmsChartDate"]/text()')

url = 'https://www.ig.com/uk/ig-indices/germany-30'

headers = {
    'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.155 Safari/537.36',
    'cache-control': 'private, max-age=0, no-cache'
}
page = requests.get(url, headers=headers)
tree = html.fromstring(page.content)
price = "{ \"price\":\"" + returnValueAsString(tree, '//*[@id="ofr"]/text()') + "}"
#timeS = ",\"time\":\"" + returnValueAsString(tree, '//*[@id="pageQuotes"]/div[2]/div[1]/table/tbody/tr[2]/td[10]/span/text()') + str(" }")
#dateS = time.strftime("%Y-%m-%d")

print(price)
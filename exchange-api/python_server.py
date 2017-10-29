#!/usr/bin/env python
 
from http.server import BaseHTTPRequestHandler, HTTPServer
from yahoo_historical import Fetcher
from urllib.parse import urlparse, parse_qs
 
# HTTPRequestHandler class
class testHTTPServer_RequestHandler(BaseHTTPRequestHandler):
 
  # GET
  def do_GET(self):
        # Send response status code
        self.send_response(200)
 
        # Send headers
        self.send_header('Content-type','text/json')
        self.end_headers()

        query = parse_qs(urlparse(self.path).query)
        #print(str(query['ticker']))
        # Send message back to client
        data = Fetcher('^GDAXI', [2016,1,1], [2017,1,1])
        d = data.getHistorical()
        d.to_json('data.csv')
        f = open('data.csv', 'r')
        # Write content as utf-8 data
        self.wfile.write(f.read().encode('utf-8'))
        f.close()
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
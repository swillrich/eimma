'use strict';

//const express = require('express');

//following https://nodejs.org/api/net.html#net_net
var net = require('net');

// Constants
const PORT = 8081;
const HOST = '0.0.0.0';

// App
//const app = express();
//app.get('/', (req, res) => {
//  res.send('Hello ElMMa\n');
//});

//------------------------------------------[
// Create an instance of the Server and waits for a conexão
net.createServer(function(sock) {


  // Receives a connection - a socket object is associated to the connection automatically
  console.log('CONNECTED: ' + sock.remoteAddress +':'+ sock.remotePort);


  // Add a 'data' - "event handler" in this socket instance
  sock.on('data', function(data) {
	  // data was received in the socket 
	  // Writes the received message back to the socket (echo)
	  sock.write(data);
  });


  // Add a 'close' - "event handler" in this socket instance
  sock.on('close', function(data) {
	  // closed connection
	  console.log('CLOSED: ' + sock.remoteAddress +' '+ sock.remotePort);
  });


}).listen(PORT, HOST);


console.log('ElMMas dispatcher nodejs server is listening on ' + HOST +':'+ PORT);
//------------------------------------------]

//app.listen(PORT, HOST);
//console.log(`Running on http://${HOST}:${PORT}`);
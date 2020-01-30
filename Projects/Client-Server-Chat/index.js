//server.js

const express = require('express');
const app = express();
const server = require('http').Server(app);
const io = require('socket.io')(server);
const path = require('path');
const port = process.env.PORT || 8000;

//static files 
app.use(express.static(path.join(__dirname, 'src')));


app.get('/', function(req, res)
{
  res.sendFile(__dirname + '/index.html');
});


  io.on('connection', function (socket) 
  {
      //when a player connects 
    socket.on('player', function(id)
    {
        socket.id = id;
        console.log("new user connected, named: " + socket.id);
        io.emit('player', socket.id + ' :has joined the chat ');
    });

    //when a plauyer disconnects
    socket.on('disconnect', function(oldname)
    {
        socket.oldname = oldname;
        console.log("user: [ " + socket.id + ' ]has disconnected');
        io.emit('player', socket.id + ' :has left the chat! ');
    });
    
    // sending messages
    socket.on('sms', function (msgObj) 
    {
      io.emit('sms', msgObj);
    });

  });

server.listen(port, function()
{    
  console.log('listening on port ' + port);
});
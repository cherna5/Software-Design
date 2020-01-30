$(function () {

    var socket = io();
    var id = '';
    var charInput = $('#enterName');
    var chatInput = $('#chat');
  
    //---------------------------------------------------
    //event handler for Entering name when pressed "enter"
    charInput.keydown(function(e) 
    {
      //13 is for "enter"
      if (e.keyCode == 13) 
      {
        e.preventDefault();
  
        if (charInput.val() !== '') 
        {//no null 
          id = charInput.val();
          charInput.val('');
          $('.userInput').hide();
          socket.emit('player', id);
        }

      }
    });
  
    //event handler for Entering text when pressed "enter"
    chatInput.keydown(function(e) 
    {
      if (e.keyCode == 13) 
      {//no null 
        e.preventDefault();
  
        if (chatInput.val() !== '' && id !== '') 
        {
          socket.emit('sms', {name: id, displayTxt: chatInput.val()});
          chatInput.val('');
        }
      }
    });
    //---------------------------------------------------
  
    // event handler for Entering text when clicked 
    $('.buttonChatMessage').on('click', function(e) 
    {
      e.preventDefault();
  
      if (chatInput.val() !== '' && id !== '') 
      {//no null 
        socket.emit('sms', {name: id, displayTxt: chatInput.val()});
        chatInput.val('');
      }
    });
  
    //---------------------------------------------------
    // handler for incoming messages
    socket.on('sms', function(msgObj)
    {
      $('#messages').append($('<div class="displayTxt newSMS">').html('<span class="dPlayerName">' + msgObj.name + '</span>: ' + msgObj.displayTxt));
      $('.chatBox').scrollTop($('#messages').height());
    });
  
    // handleer for incoming player to html 
    socket.on('player', function(id)
    {
      $('#messages').append($('<div class="displayTxt newPlayer">').text(id));
      $('.chatBox').scrollTop($('#messages').height());
    });

});  

var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);

app.get('/', function(req, res){
	res.sendFile(__dirname+'/index.html');

});

io.on('connection', function(socket){
	console.log('a user connected: ' + socket.id);

	socket.on('play', function(data){
		io.to(data).emit('play', 1);
		
	});

	socket.on('pause', function(data){
		io.to(data).emit('pause', 1);
		
	});

	
	socket.on('startVideo', function(data){
		io.to(data.to).emit('startVideo', data.url);
		
	});

	io.to(socket.id).emit('key', socket.id);


	socket.on('disconnect', function(){
		console.log('a user disconnected: ' + socket.id);
	});

});

http.listen(process.env.PORT || 3000, function(){
	console.log('server running');
});



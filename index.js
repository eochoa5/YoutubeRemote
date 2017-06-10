var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);

app.get('/', function(req, res){
	res.sendFile(__dirname+'/index.html');

});

io.on('connection', function(socket){
	console.log('a user connected: ' + socket.id);

	io.to(socket.id).emit('key', socket.id);

	socket.on('play', function(data){
		io.to(data).emit('play', 1);
		
	});

	socket.on('pause', function(data){
		io.to(data).emit('pause', 1);
		
	});


	socket.on('startVideo', function(data){
		io.to(data.to).emit('startVideo', {url:data.url, sender:socket.id});

	});

	socket.on('vidDuration', function(data){
		io.to(data.to).emit('vidDuration', data.duration);

	});

	socket.on('setVolume', function(data){
		io.to(data.to).emit('setVolume', data.vol);
		
	});

	socket.on('seekTo', function(data){
		io.to(data.to).emit('seekTo', data.secs);
		
	});

	socket.on('mute', function(data){
		io.to(data).emit('mute', 1);
		
	});

	socket.on('unMute', function(data){
		io.to(data).emit('unMute', 1);
		
	});
	
	socket.on('disconnect', function(){
		console.log('a user disconnected: ' + socket.id);
	});

});

http.listen(process.env.PORT || 3000, function(){
	console.log('server running');
});



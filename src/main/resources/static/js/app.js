var ws;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#incomings").removeAttr('hidden');
    $("#incomings").html("message waiting ...");
}

function connect() {
    var ip = location.host;
    var socket = new WebSocket("wss://" + ip + "/websocket");
    ws = Stomp.over(socket);

    ws.connect({}, function () {
        ws.subscribe("/queue/reply", function (message) {
            showIncoming(message.body);
        });
        setConnected(true);
    }, function () {
        if (ws != null) {
            ws.close();
        }
    });
}

function showIncoming(message) {
    var json = JSON.parse(message);
    var prev = $("#incomings")
        .html()
    $("#incomings")
        .html(prev + "<br>" + json.message)
}

$(function () {
    $("#connect").click(function (e) {
        e.preventDefault();
        connect();
    });
    $("#incomings").click(function () {
        location.assign($(this).attr('href'));
    });
});

function connectWait() {
    window.setTimeout(connect, 1000);
}

window.onload = connectWait;

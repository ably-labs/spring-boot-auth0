// Connect to Ably, getting an Ably Token from our Spring Boot server
const ably = new Ably.Realtime({
    authUrl: '/ably-token'
});

// Instantiate an object for the 'chat' channel
const channel = ably.channels.get('[?rewind=20]chat');

// Subscribe for messages sent to the channel
channel.subscribe(function(message) {
    show(message.data, message.timestamp, message.clientId);
});

// Enter the channel's presence set using our clientID
channel.presence.enter();

// Update our list of clients whenever the presence set changes
channel.presence.subscribe(function(presenceMsg) {
    channel.presence.get(function(err, members) {
        if(err) { return console.log("Error fetching presence data: " + err); }
        document.getElementById('presence-members').innerHTML = members.map(function(member) {
            return '<li>' + member.clientId + '</li>';
        }).join("");
    });
});

const sendButton =  document.getElementById("publish");
const inputField = document.getElementById("input-field");

//Add an event listener to check when the send button is clicked
sendButton.addEventListener('click', function() {
    const input = inputField.value;
    inputField.value = "";
    let date = new Date();
    let timestamp = date.getTime();

    // Publish the message to the Ably Channel
    channel.publish('message', input);
  });

function show(text, timestamp, currentUser) {
    const time = getTime(timestamp);
    const messageItem = `<li class="message ${currentUser === ably.auth.clientId ? "sent-message": ""}">
        <div class="message-info">
            <h5 class="message-name">${time} - Tom</h5>
            <p class="message-text">${text}</p>
        </div>
    </li>`
    document.getElementById("channel-status").innerHTML = document.getElementById("channel-status").innerHTML + messageItem;
    document.getElementById("msg-container").scrollTop = document.getElementById("msg-container").scrollHeight;
}

function getTime(unix_timestamp) {
    var date = new Date(unix_timestamp);
    var hours = date.getHours();
    var minutes = "0" + date.getMinutes();
    var seconds = "0" + date.getSeconds();
    var formattedTime = hours + ':' + minutes.substr(-2) + ':' + seconds.substr(-2);
    return formattedTime;
}

const elements = {
    localVideo: document.getElementById("localVideo"),
    remoteVideo: document.getElementById("remoteVideo"),
    localIdInp: document.getElementById("localId"),
    remoteIdInp: document.getElementById("remoteId"),
    connectBtn: document.getElementById("connectBtn"),
    callBtn: document.getElementById("callBtn"),
    testConnection: document.getElementById("testConnection")
};

let localStream, remoteStream, localPeer, remoteID, localID, stompClient;

// ICE Server Configuration
const iceServers = {iceServers: [{urls: "stun:stun.l.google.com:19302"}]};

function initLocalPeer() {
    localPeer = new RTCPeerConnection(iceServers);

    localPeer.ontrack = event => {
        elements.remoteVideo.srcObject = event.streams[0];
    };

    localPeer.onicecandidate = event => {
        if (event.candidate) {
            const candidate = {
                type: "candidate",
                label: event.candidate.sdpMLineIndex,
                id: event.candidate.candidate
            };
            sendCandidate(candidate);
        }
    };
}

function sendCandidate(candidate) {
    console.log("Sending Candidate", candidate);
    stompClient.send("/app/candidate", {}, JSON.stringify({
        toUser: remoteID,
        fromUser: localID,
        candidate: candidate
    }));
}

function setupLocalStream() {
    navigator.mediaDevices.getUserMedia({video: true, audio: true})
        .then(stream => {
            localStream = stream;
            elements.localVideo.srcObject = stream;
        })
        .catch(error => console.error("Error accessing media devices:", error));
}

function handleOffer(offerBody) {
    const offer = JSON.parse(offerBody).offer;
    localPeer.setRemoteDescription(new RTCSessionDescription(offer));
    localPeer.createAnswer().then(description => {
        localPeer.setLocalDescription(description);
        console.log("Setting Local Description", description);
        stompClient.send("/app/answer", {}, JSON.stringify({
            toUser: remoteID,
            fromUser: localID,
            answer: description
        }));
    });
}

function handleAnswer(answerBody) {
    const answer = JSON.parse(answerBody).answer;
    localPeer.setRemoteDescription(new RTCSessionDescription(answer));
}

function handleCandidate(candidateBody) {
    const candidate = JSON.parse(candidateBody).candidate;
    const iceCandidate = new RTCIceCandidate({
        sdpMLineIndex: candidate.label,
        candidate: candidate.id
    });
    localPeer.addIceCandidate(iceCandidate);
}

function addTracksToPeer() {
    localStream.getTracks().forEach(track => localPeer.addTrack(track, localStream));
}

function connectToWebSocket() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    localID = elements.localIdInp.value;

    stompClient.connect({}, frame => {
        console.log("Connected:", frame);

        stompClient.subscribe(`/topic/test-server`, message => console.log("Received:", message.body));
        stompClient.subscribe(`/user/${localID}/topic/call`, message => {
            remoteID = message.body;
            addTracksToPeer();
            localPeer.createOffer().then(description => {
                localPeer.setLocalDescription(description);
                stompClient.send("/app/offer", {}, JSON.stringify({
                    toUser: remoteID,
                    fromUser: localID,
                    offer: description
                }));
            });
        });

        stompClient.subscribe(`/user/${localID}/topic/offer`, message => handleOffer(message.body));
        stompClient.subscribe(`/user/${localID}/topic/answer`, message => handleAnswer(message.body));
        stompClient.subscribe(`/user/${localID}/topic/candidate`, message => handleCandidate(message.body));
    });
}

function initiateCall() {
    remoteID = elements.remoteIdInp.value;
    stompClient.send("/app/call", {}, JSON.stringify({
        callTo: remoteID,
        callFrom: localID
    }));
}

function testServerConnection() {
    stompClient.send("/app/testServer", {}, "Test Server");
}

function initializeEventListeners() {
    elements.connectBtn.addEventListener('click', connectToWebSocket);
    elements.callBtn.addEventListener('click', initiateCall);
    elements.testConnection.addEventListener('click', testServerConnection);
}

// Initialize everything
initLocalPeer();
setupLocalStream();
initializeEventListeners();

const elements = {
    localVideo: document.getElementById("localVideo"),
    remoteVideo: document.getElementById("remoteVideo"),
    micToggleBtn: document.getElementById("micToggleBtn"),
    camToggleBtn: document.getElementById("camToggleBtn"),
    endCallBtn: document.getElementById("endCallBtn")
};

let localStream, localPeer, stompClient;
let isMicActive = true;
let isCamActive = true;

const url = 'https://ws.deploy-hirexptit.io.vn'

const iceServers = {iceServers: [{urls: "stun:stun.l.google.com:19302"}]};

async function initLocalPeer() {
    localPeer = new RTCPeerConnection(iceServers);
    const stream = await navigator.mediaDevices.getUserMedia({video: true, audio: true});
    localStream = stream;
    elements.localVideo.srcObject = stream;
    console.log("Local stream setup complete!!!");
    connectToWebSocket();

    // Add event listeners for control buttons
    elements.micToggleBtn.addEventListener('click', toggleMicrophone);
    elements.camToggleBtn.addEventListener('click', toggleCamera);
    elements.endCallBtn.addEventListener('click', endCall);
}

function toggleMicrophone() {
    isMicActive = !isMicActive;
    const audioTracks = localStream.getAudioTracks();

    audioTracks.forEach(track => {
        track.enabled = isMicActive;
    });

    // Update button style
    if (isMicActive) {
        elements.micToggleBtn.classList.remove('inactive');
        elements.micToggleBtn.classList.add('active');
    } else {
        elements.micToggleBtn.classList.remove('active');
        elements.micToggleBtn.classList.add('inactive');
    }
}

function toggleCamera() {
    isCamActive = !isCamActive;
    const videoTracks = localStream.getVideoTracks();

    videoTracks.forEach(track => {
        track.enabled = isCamActive;
    });

    // Update button style and video visibility
    if (isCamActive) {
        elements.camToggleBtn.classList.remove('inactive');
        elements.camToggleBtn.classList.add('active');
        elements.localVideo.style.display = 'block';
    } else {
        elements.camToggleBtn.classList.remove('active');
        elements.camToggleBtn.classList.add('inactive');
        elements.localVideo.style.display = 'none';
    }
}

function endCall(isReceiver) {
    // Close the peer connection
    if (localPeer) {
        localPeer.close();
    }

    // Stop all tracks in the local stream
    if (localStream) {
        localStream.getTracks().forEach(track => track.stop());
    }

    // Send end call signal to the other user (optional)
    if (stompClient && fromUser && toUser && !(isReceiver === true)) {
        console.log("SENDING END CALL SIGNAL......");
        stompClient.send("/app/end-call", {}, JSON.stringify({
            fromUser: fromUser,
            toUser: toUser
        }));
    }

    // Disconnect from WebSocket
    if (stompClient) {
        stompClient.disconnect();
    }

    // Close the window or redirect
    window.close(); // or window.location.href = 'some_page.html';
}

function connectToWebSocket() {
    console.log("Connecting to websocket:", url);
    const socket = new SockJS(url + '/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, frame => {
        console.log("Connected to WebSocket:", isCallee);

        stompClient.subscribe(`/user/${fromUser}/topic/end-call`, message => {
            alert('Call ended by the other user');
            endCall(true);
        });

        stompClient.subscribe(`/user/${fromUser}/topic/call`, message => {
            toUser = message.body;

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
                    stompClient.send("/app/candidate", {}, JSON.stringify({
                        toUser: toUser,
                        fromUser: fromUser,
                        candidate: candidate
                    }));
                }
            };

            localStream.getTracks().forEach(track => localPeer.addTrack(track, localStream));

            localPeer.createOffer().then(description => {
                localPeer.setLocalDescription(description);
                stompClient.send("/app/offer", {}, JSON.stringify({
                    toUser: toUser,
                    fromUser: fromUser,
                    offer: description
                }));
            });
        });

        stompClient.subscribe(`/user/${fromUser}/topic/offer`, message => {
            const offer = JSON.parse(message.body).offer;

            localPeer.ontrack = (event) => {
                elements.remoteVideo.srcObject = event.streams[0]
            }

            localPeer.onicecandidate = (event) => {
                if (event.candidate) {
                    var candidate = {
                        type: "candidate",
                        label: event.candidate.sdpMLineIndex,
                        id: event.candidate.candidate,
                    }
                    console.log("Sending Candidate")
                    console.log(candidate)
                    stompClient.send("/app/candidate", {}, JSON.stringify({
                        "toUser": toUser,
                        "fromUser": fromUser,
                        "candidate": candidate
                    }))
                }
            }

            localStream.getTracks().forEach(track => {
                localPeer.addTrack(track, localStream);
            });

            localPeer.setRemoteDescription(new RTCSessionDescription(offer));

            localPeer.createAnswer().then(description => {
                localPeer.setLocalDescription(description);
                console.log("Setting Local Description", description);
                stompClient.send("/app/answer", {}, JSON.stringify({
                    toUser: toUser,
                    fromUser: fromUser,
                    answer: description
                }));
            });
        });

        stompClient.subscribe(`/user/${fromUser}/topic/answer`, message => {
            const offer = JSON.parse(message.body).answer;
            localPeer.setRemoteDescription(new RTCSessionDescription(offer));
        });

        stompClient.subscribe(`/user/${fromUser}/topic/candidate`, message => {
            const offer = JSON.parse(message.body).candidate;
            const iceCandidate = new RTCIceCandidate({
                sdpMLineIndex: offer.label,
                candidate: offer.id
            });
            localPeer.addIceCandidate(iceCandidate);
        });

        stompClient.subscribe(`/user/${fromUser}/topic/accept`, message => {
            const status = JSON.parse(message.body).status;
            if (status === 'VIDEO_CALL_RESPONSE_ACCEPT') {
                console.log("====ACCEPT READY SIGNAL====")
                initiateCall();
            } else {
                window.confirm("The call was refused by the callee. Do you want to close the window?");
                window.close();
            }
        });

        if (isCallee == '1') {
            const acceptPayload = {
                fromUser: fromUser,
                toUser: toUser,
                status: 'VIDEO_CALL_RESPONSE_ACCEPT',
            };
            stompClient.send("/app/accept", {}, JSON.stringify(acceptPayload));
            console.log('SENDING READY SIGNAL..........');
        }
    });
}

function initiateCall() {
    console.log("Sending call request from", fromUser, "to", toUser);
    stompClient.send("/app/call", {}, JSON.stringify({
        callTo: toUser,
        callFrom: fromUser
    }));
}

// elements.callBtn.addEventListener('click', initiateCall);

initLocalPeer();

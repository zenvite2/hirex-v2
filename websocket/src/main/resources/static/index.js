const elements = {
    localVideo: document.getElementById("localVideo"),
    remoteVideo: document.getElementById("remoteVideo"),
    micToggleBtn: document.getElementById("micToggleBtn"),
    camToggleBtn: document.getElementById("camToggleBtn"),
    endCallBtn: document.getElementById("endCallBtn"),
    shareScreenBtn: document.getElementById("shareScreenBtn")
};

let localStream, localPeer, stompClient, originalStream;
let isMicActive = true;
let isCamActive = true;
let isScreenSharing = false;

const mediaState = {
    originalCameraStream: null,
    currentStream: null,
    screenStream: null
};

const url = 'https://192.168.1.123:8888'

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
    elements.shareScreenBtn.addEventListener('click', toggleScreenSharing);

    // Additional error handling for screen sharing
    window.addEventListener('unhandledrejection', (event) => {
        if (event.reason.name === 'NotAllowedError') {
            stopScreenSharing();
        }
    });
}

async function toggleScreenSharing() {
    // Prevent screen sharing if camera is off
    if (!isCamActive) {
        alert("Please turn on camera before screen sharing.");
        return;
    }

    if (!isScreenSharing) {
        try {
            // Capture screen stream
            const screenStream = await navigator.mediaDevices.getDisplayMedia({
                video: true,
                audio: false
            });

            // Store the original camera stream if not already stored
            if (!mediaState.originalCameraStream) {
                mediaState.originalCameraStream = localStream;
            }

            // Store screen stream
            mediaState.screenStream = screenStream;

            // Replace video track in peer connection
            replaceVideoTrack(screenStream.getVideoTracks()[0]);

            // Update local video display
            elements.localVideo.srcObject = screenStream;
            localStream = screenStream;

            // Update UI
            elements.shareScreenBtn.classList.add('active');
            elements.camToggleBtn.disabled = true;
            isScreenSharing = true;

            // Handle screen sharing ending
            screenStream.getVideoTracks()[0].onended = stopScreenSharing;

        } catch (err) {
            console.error("Screen sharing error:", err);

            // Reset if user cancels
            if (err.name === 'NotAllowedError') {
                return;
            }
        }
    } else {
        stopScreenSharing();
    }
}

function stopScreenSharing() {
    if (isScreenSharing && mediaState.originalCameraStream) {
        // Restore original camera stream
        replaceVideoTrack(mediaState.originalCameraStream.getVideoTracks()[0]);

        // Update local video back to camera stream
        elements.localVideo.srcObject = mediaState.originalCameraStream;
        localStream = mediaState.originalCameraStream;

        // Clean up
        if (mediaState.screenStream) {
            mediaState.screenStream.getTracks().forEach(track => track.stop());
            mediaState.screenStream = null;
        }

        // Reset UI
        elements.shareScreenBtn.classList.remove('active');
        elements.camToggleBtn.disabled = false;
        isScreenSharing = false;
    }
}

function replaceVideoTrack(newVideoTrack) {
    // Find video sender in peer connection
    const sender = localPeer.getSenders().find(s => s.track.kind === 'video');

    if (sender) {
        sender.replaceTrack(newVideoTrack).then(r => {});
    }
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
    // Prevent camera toggle during screen sharing
    if (isScreenSharing) {
        alert("Please stop screen sharing first.");
        return;
    }

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

    if (isScreenSharing) {
        stopScreenSharing();
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
            alert('Cuộc gọi đã được tắt bởi người khác.');
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
                alert("Người nhận từ chối nhận cuộc gọi. Đóng cửa sổ?");
                endCall(true);
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

package com.ptit.websocket.controller;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class WebRTCController {

    /**
     * simpMessagingTemplate is an instance of {@link SimpMessagingTemplate} that facilitates
     * sending messages using the Simple Messaging Protocol (STOMP) over WebSocket.
     *
     * It provides methods to send messages to a specific user, to a specific destination, or broadcast
     * messages to multiple subscribers. simpMessagingTemplate is typically used in real-time
     * messaging or notification systems to communicate with WebSocket-connected clients.
     *
     * The {@code @Autowired} annotation enables Spring to automatically inject an instance of
     * SimpMessagingTemplate at runtime, which is configured and managed by the Spring context.
     */
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    /**
     * Handles the GET request for the video call page and sets the necessary model attributes.
     *
     * @param fromUser the identifier of the user initiating the request
     * @param toUser the identifier of the user receiving the request
     * @param isCallee a flag indicating whether the current user is the callee (1 if true, or 0 if false)
     * @param model the Model object used to add attributes for the view
     * @return the name of the view to be rendered ("index")
     */
    @GetMapping("/video-call")
    public String index(
            @RequestParam(name = "fromUser") String fromUser,
            @RequestParam(name = "toUser") String toUser,
            @RequestParam(name = "isCallee") Integer isCallee,
            Model model
    ) {
        model.addAttribute("fromUser", fromUser);
        model.addAttribute("toUser", toUser);
        model.addAttribute("isCallee", isCallee);
        log.info("Video call redirect: \n{\n    fromUser: {}\n    toUser: {}\n    isCallee: {}\n}", fromUser, toUser, isCallee);
        return "index";
    }

    /**
     * Handles redirection of a call message.
     * Processes the incoming call message, logs the relevant call details,
     * and sends the call information to a specified user.
     *
     * @param call the JSON string containing call details,
     *             which includes "callTo" (the recipient of the call)
     *             and "callFrom" (the caller's information)
     */
    @MessageMapping("/call")
    public void callRedirect(String call) {
        JSONObject jsonObject = new JSONObject(call);
        log.info("Call redirect:\n" +
                "{\n" +
                "  \"callTo\": \"" + jsonObject.get("callTo") + "\",\n" +
                "  \"callFrom\": \"" + jsonObject.get("callFrom") + "\",\n" +
                "}"
        );
        simpMessagingTemplate.convertAndSendToUser(jsonObject.getString("callTo"), "/topic/call", jsonObject.get("callFrom"));
    }

    /**
     * Processes an incoming WebSocket offer message and redirects it to the
     * intended recipient user.
     *
     * @param offer A JSON string containing the offer details. It is expected to
     *              have the fields "fromUser", "toUser", and "offer".
     */
    @MessageMapping("/offer")
    public void offerRedirect(String offer) {
        JSONObject jsonObject = new JSONObject(offer);
        log.info("Offer redirect:\n" +
                "{\n" +
                "  \"fromUser\": \"" + jsonObject.get("fromUser") + "\"\n" +
                "  \"toUser\": \"" + jsonObject.get("toUser") + "\",\n" +
                "  \"offer\": \"" + jsonObject.get("offer") + "\",\n" +
                "}"
        );
        simpMessagingTemplate.convertAndSendToUser(jsonObject.getString("toUser"), "/topic/offer", offer);
    }

    /**
     * Handles incoming messages containing an "answer" and redirects it to the specified user.
     *
     * @param answer A JSON-encoded string representing the answer message. It must contain the following fields:
     *               - "fromUser": The username of the user who sent the answer.
     *               - "toUser": The username of the user who should receive the answer.
     *               - "answer": The answer message content.
     */
    @MessageMapping("/answer")
    public void answerRedirect(String answer) {
        JSONObject jsonObject = new JSONObject(answer);
        log.info("Answer redirect:\n" +
                "{\n" +
                "  \"fromUser\": \"" + jsonObject.get("fromUser") + "\",\n" +
                "  \"toUser\": \"" + jsonObject.get("toUser") + "\",\n" +
                "  \"answer\": \"" + jsonObject.get("answer") + "\"\n" +
                "}"
        );
        simpMessagingTemplate.convertAndSendToUser(jsonObject.getString("toUser"), "/topic/answer", answer);
    }

    /**
     * Handles and processes an accept redirect message.
     *
     * @param accept The JSON string containing redirect information such as
     *               the user sending the request ("fromUser"), the user receiving the request ("toUser"),
     *               and the current status ("status").
     */
    @MessageMapping("/accept")
    public void acceptRedirect(String accept) {
        JSONObject jsonObject = new JSONObject(accept);
        log.info("Accept redirect:\n" +
                "{\n" +
                "  \"fromUser\": \"" + jsonObject.get("fromUser") + "\",\n" +
                "  \"toUser\": \"" + jsonObject.get("toUser") + "\",\n" +
                "  \"status\": \"" + jsonObject.get("status") + "\"\n" +
                "}"
        );
        simpMessagingTemplate.convertAndSendToUser(jsonObject.getString("toUser"), "/topic/accept", accept);
    }

    /**
     * Handles incoming WebSocket messages containing candidate information,
     * processes the message, and redirects it to the designated user.
     *
     * @param candidate JSON formatted String containing the candidate information
     *                  including details about the sender, receiver, and candidate data.
     */
    @MessageMapping("/candidate")
    public void candidateRedirect(String candidate) {
        JSONObject jsonObject = new JSONObject(candidate);
        log.info("Candidate redirect:\n" +
                "{\n" +
                "  \"fromUser\": \"" + jsonObject.get("fromUser") + "\",\n" +
                "  \"toUser\": \"" + jsonObject.get("toUser") + "\",\n" +
                "  \"candidate\": \"" + jsonObject.get("candidate") + "\"\n" +
                "}"
        );
        simpMessagingTemplate.convertAndSendToUser(jsonObject.getString("toUser"), "/topic/candidate", candidate);
    }

    /**
     * Handles the end of a call by processing the provided JSON payload.
     * The method relays the provided information to the specified user.
     *
     * @param endCall a JSON string containing details about the call to be ended.
     *                It must contain "fromUser" and "toUser" keys to specify the
     *                initiator and the recipient of the call respectively.
     */
    @MessageMapping("/end-call")
    public void endCall(String endCall) {
        JSONObject jsonObject = new JSONObject(endCall);
        log.info("End call redirect:\n" +
                "{\n" +
                "  \"fromUser\": \"" + jsonObject.get("fromUser") + "\",\n" +
                "  \"toUser\": \"" + jsonObject.get("toUser") + "\"\n" +
                "}"
        );
        simpMessagingTemplate.convertAndSendToUser(jsonObject.getString("toUser"), "/topic/end-call", endCall);
    }
}

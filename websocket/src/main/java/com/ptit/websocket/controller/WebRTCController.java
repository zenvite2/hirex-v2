package com.ptit.websocket.controller;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class WebRTCController {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/video-call")
    public String index(
//            @RequestParam(name = "fromUser") String fromUser,
//            @RequestParam(name = "toUser") String toUser,
//            Model model
    ) {

//        model.addAttribute("fromUser", fromUser);
//        model.addAttribute("toUser", toUser);
//        log.info("Video call setup: {\nfromUser: {}\ntoUser: {}\n}", fromUser, toUser);
        return "index";
    }

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
}

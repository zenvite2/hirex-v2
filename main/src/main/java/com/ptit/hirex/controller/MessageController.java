package com.ptit.hirex.controller;

import com.ptit.data.entity.User;
import com.ptit.hirex.dto.UserConversationsDto;
import com.ptit.hirex.enums.StatusCodeEnum;
import com.ptit.hirex.model.ResponseBuilder;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @ResponseBody
    @GetMapping("/conversations")
    public ResponseEntity<ResponseDto<List<UserConversationsDto>>> getUserConversations(@AuthenticationPrincipal User user) {
        System.out.println(user.toString());
        List<UserConversationsDto> conversations = messageService.getUserConversations(user.getId());
        return ResponseBuilder.okResponse("Query success", conversations, StatusCodeEnum.MESSAGE1000);
    }
}

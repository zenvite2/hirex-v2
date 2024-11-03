package com.ptit.hirex.controller;

import com.ptit.hirex.dto.request.ReplyRequest;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/replies")
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping()
    public ResponseEntity<ResponseDto<Object>> createReply(@RequestBody ReplyRequest replyRequest) {
        return replyService.createReply(replyRequest);
    }
}

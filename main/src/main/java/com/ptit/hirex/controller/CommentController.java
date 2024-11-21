package com.ptit.hirex.controller;

import com.ptit.hirex.dto.request.CommentRequest;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{companyId}")
    public ResponseEntity<ResponseDto<Object>> getCommentsByCompanyId(@PathVariable Long companyId) {
        return commentService.getCommentsByCompanyId(companyId);
    }

    @PostMapping()
    public ResponseEntity<ResponseDto<Object>> createComment(@RequestBody CommentRequest commentRequest) {
        return commentService.createComment(commentRequest);
    }
}

package com.ptit.hirex.service;

import com.ptit.data.entity.Comment;
import com.ptit.data.entity.Reply;
import com.ptit.data.entity.User;
import com.ptit.data.repository.CommentRepository;
import com.ptit.data.repository.ReplyRepository;
import com.ptit.data.repository.UserRepository;
import com.ptit.hirex.dto.request.ReplyRequest;
import com.ptit.hirex.enums.StatusCodeEnum;
import com.ptit.hirex.model.ResponseBuilder;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.security.service.AuthenticationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final CommentRepository commentRepository;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final LanguageService languageService;

    public ResponseEntity<ResponseDto<Object>> createReply(ReplyRequest replyRequest) {
        String userName = authenticationService.getUserFromContext();

        Optional<User> userOptional = userRepository.findByUsername(userName);

        if (userOptional.isEmpty()) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("auth.signup.user.not.found"),
                    StatusCodeEnum.AUTH0016
            );
        }

        try {
            Reply reply = new Reply();
            reply.setUserId(userOptional.get().getId());
            reply.setContent(replyRequest.getContent());

            if (replyRequest.getParentReplyId() != null) {
                Reply parentReply = replyRepository.findById(replyRequest.getParentReplyId())
                        .orElseThrow(() -> new EntityNotFoundException("Parent reply not found"));
                reply.setParentReply(parentReply);
            } else {
                Comment parentComment = commentRepository.findById(replyRequest.getCommentId())
                        .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
                reply.setParentComment(parentComment);
            }

            replyRepository.save(reply);
            return ResponseBuilder.okResponse(
                    languageService.getMessage("create.reply.success"),
                    reply,
                    StatusCodeEnum.REPLY1000
            );
        } catch (RuntimeException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("create.reply.failed"),
                    StatusCodeEnum.REPLY0000
            );
        }
    }
}

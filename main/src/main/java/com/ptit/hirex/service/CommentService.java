package com.ptit.hirex.service;

import com.ptit.data.entity.Comment;
import com.ptit.data.entity.Notification;
import com.ptit.data.entity.User;
import com.ptit.data.repository.CommentRepository;
import com.ptit.data.repository.UserRepository;
import com.ptit.hirex.dto.request.CommentRequest;
import com.ptit.hirex.enums.StatusCodeEnum;
import com.ptit.hirex.model.ResponseBuilder;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final AuthenticationService authenticationService;
    private final LanguageService languageService;
    private final UserRepository userRepository;

    public ResponseEntity<ResponseDto<Object>> createComment(CommentRequest commentRequest) {
//        String userName = authenticationService.getUserFromContext();
//
//        Optional<User> userOptional = userRepository.findByUsername(userName);
//
//        if (userOptional.isEmpty()) {
//            return ResponseBuilder.badRequestResponse(
//                    languageService.getMessage("auth.signup.user.not.found"),
//                    StatusCodeEnum.AUTH0016
//            );
//        }

        try {
            Comment comment = new Comment();
            comment.setCompanyId(commentRequest.getCompanyId());
            comment.setUserId(commentRequest.getUserId());
            comment.setContent(commentRequest.getContent());
            comment.setUsername(userRepository.findById(commentRequest.getUserId()).get().getUsername());

            commentRepository.save(comment);
            return ResponseBuilder.okResponse(
                    languageService.getMessage("create.comment.success"),
                    comment,
                    StatusCodeEnum.COMMENT1000
            );
        } catch (RuntimeException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("create.comment.failed"),
                    StatusCodeEnum.COMMENT0000
            );
        }
    }

    public ResponseEntity<ResponseDto<Object>> getCommentsByCompanyId(Long companyId) {
        try {
            List<Comment> comments = commentRepository.findByCompanyId(companyId, Sort.by(Sort.Order.desc("createdAt")));

            return ResponseBuilder.okResponse(
                    languageService.getMessage("get.comment.success"),
                    comments,
                    StatusCodeEnum.COMMENT1001
            );
        } catch (RuntimeException e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("get.comment.failed"),
                    StatusCodeEnum.COMMENT0001
            );
        }
    }
}

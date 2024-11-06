package com.ptit.hirex.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyRequest {
    private Long commentId;
    private String content;
    private Long parentReplyId;
}

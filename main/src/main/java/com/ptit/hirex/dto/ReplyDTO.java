package com.ptit.hirex.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDTO {
    private Long id;
    private String content;
    private Long userId;
    private List<ReplyDTO> childReplies;
}

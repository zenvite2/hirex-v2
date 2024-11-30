package com.ptit.hirex.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserConversationsDto {
    /**
     * User id chatting with
     */
    private Long userId;
    private String username;
    private String fullName;
    private String avtUrl;
    private List<MessageDto> last10Messages;
}

package com.ptit.hirex.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillResponse {
    private String skillName;

    private Long techId;

    private String techName;

    private String level;

    private String description;
}

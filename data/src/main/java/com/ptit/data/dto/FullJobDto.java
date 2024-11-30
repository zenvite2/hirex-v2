package com.ptit.data.dto;

import com.ptit.data.entity.*;
import lombok.*;

import java.util.List;

@ToString
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class FullJobDto {
    private Long id;
    /**
     * Tên job
     */
    private String title;
    /**
     * Mô tả job
     */
    private String description;

    /**
     * Quyền lợi, lợi ích của job
     */
    private String benefit;

    /**
     * Yêu cầu ứng viên
     */
    private String requirement;

    /**
     * Địa chỉ làm việc
     */
    private String location;

    /**
     * Mức lương
     */
    private Long minSalary;

    /**
     * Mức lương
     */
    private Long maxSalary;

    private Long yearExperience;

    private City city;

    private District district;

    private Position position;

    private JobType jobType;

    private ContractType contractType;

    // han nop ho so
    private String deadline;

    private Industry industry;

    private List<Long> skill_ids;

    /**
     * Trinh do hoc van
     */
    private EducationLevel educationLevel;

    private String companyLogo;

    private String companyName;

    private String companyDescription;
}


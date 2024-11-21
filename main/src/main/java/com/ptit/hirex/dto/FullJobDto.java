package com.ptit.hirex.dto;

import com.ptit.data.base.HashMapConverter;
import com.ptit.data.entity.*;
import com.ptit.data.enums.JobStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

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

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @Column(name = "job_details", columnDefinition = "TEXT")
    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> jobDetails;
}

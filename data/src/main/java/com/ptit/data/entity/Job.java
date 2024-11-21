package com.ptit.data.entity;

import com.ptit.data.base.Auditable;
import com.ptit.data.enums.JobStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "jobs")
public class Job extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long employer;
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

    private Long cityId;

    private Long districtId;

    private Long positionId;

    private Long jobTypeId;

    private Long contractTypeId;

    // han nop ho so
    private String deadline;

    private Long industryId;

    private String email;

    /**
     * Trinh do hoc van
     */
    private Long educationLevelId;

    private String workingTime;

    @Enumerated(EnumType.STRING)
    private JobStatus status;
}

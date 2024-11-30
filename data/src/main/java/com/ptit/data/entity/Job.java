package com.ptit.data.entity;

import com.ptit.data.base.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "jobs")
public class Job extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long employer;
    /**
     * Tên job
     */
    @Column(columnDefinition = "VARCHAR(5000)")
    private String title;
    /**
     * Mô tả job
     */
    @Column(columnDefinition = "VARCHAR(5000)")
    private String description;

    /**
     * Quyền lợi, lợi ích của job
     */
    @Column(columnDefinition = "VARCHAR(5000)")
    private String benefit;

    /**
     * Yêu cầu ứng viên
     */
    @Column(columnDefinition = "VARCHAR(5000)")
    private String requirement;

    /**
     * Địa chỉ làm việc
     */
    @Column(columnDefinition = "VARCHAR(5000)")
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

    @Column(columnDefinition = "VARCHAR(5000)")
    private String workingTime;

    private Boolean active;
}

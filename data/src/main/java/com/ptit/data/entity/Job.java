package com.ptit.data.entity;

import com.ptit.data.base.Auditable;
import com.ptit.data.enums.JobStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private Long employerId;
    private String title;
    private String description;
    private String requirements;
    private String location;
    private String salaryRange;
    private String jobType;

    // han nop ho so
    private LocalDateTime deadline;

    @Enumerated(EnumType.STRING)
    private JobStatus status;
}

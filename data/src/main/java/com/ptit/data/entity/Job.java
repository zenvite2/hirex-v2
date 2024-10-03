package com.ptit.data.entity;

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
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;

    @ManyToOne
    @JoinColumn(name = "employer_id")
    private Employer employer;

    private String title;
    private String description;
    private String requirements;
    private String location;
    private String salaryRange;
    private String jobType;
    private LocalDateTime postedDate;
    private LocalDateTime deadline;

    @Enumerated(EnumType.STRING)
    private JobStatus status;
}

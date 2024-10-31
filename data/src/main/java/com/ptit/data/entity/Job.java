package com.ptit.data.entity;

import com.ptit.data.base.Auditable;
import com.ptit.data.base.HashMapConverter;
import com.ptit.data.enums.JobStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.HashMap;
import java.util.Map;

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
    private String title;
    private String description;
    private String requirements;
    private String location;

    private Long yearExperience;

    private Long salary;

    private Long tech;

    private Long city;

    private Long district;

    private Long position;

    private Long jobType;

    private Long contractType;

    // han nop ho so
    private String deadline;

    @Enumerated(EnumType.STRING)
    private JobStatus status;
}

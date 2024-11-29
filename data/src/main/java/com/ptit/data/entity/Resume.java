package com.ptit.data.entity;

import com.ptit.data.base.Auditable;
import com.ptit.data.converter.JSONConverter;
import com.ptit.data.converter.JSONListConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "resume")
public class Resume extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String career;

    private String hobby;

    private Long employeeId;

    private Boolean status;

    @Convert(converter = JSONListConverter.class)
    @Column(columnDefinition = "VARCHAR(5000)")
    private List<Map<String, Object>> projects;

    @Convert(converter = JSONListConverter.class)
    @Column(columnDefinition = "VARCHAR(5000)")
    private List<Map<String, Object>> certificates;

    @Convert(converter = JSONListConverter.class)
    @Column(columnDefinition = "VARCHAR(5000)")
    private List<Map<String, Object>> educations;

    @Convert(converter = JSONListConverter.class)
    @Column(columnDefinition = "VARCHAR(5000)")
    private List<Map<String, Object>> experiences;

    @Convert(converter = JSONListConverter.class)
    @Column(columnDefinition = "VARCHAR(5000)")
    private List<Map<String, Object>> skills;
}
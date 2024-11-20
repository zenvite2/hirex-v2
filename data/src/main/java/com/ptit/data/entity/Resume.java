package com.ptit.data.entity;

import com.ptit.data.converter.JSONConverter;
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
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String career;

    private String hobby;

    private Long employeeId;

    @Convert(converter = JSONConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<Map<String, Object>> projects;

    @Convert(converter = JSONConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<Map<String, Object>> certificates;

    @Convert(converter = JSONConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<Map<String, Object>> educations;

    @Convert(converter = JSONConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<Map<String, Object>> experiences;

    @Convert(converter = JSONConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<Map<String, Object>> skills;
}
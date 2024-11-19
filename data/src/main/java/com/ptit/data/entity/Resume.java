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

    @Convert(converter = JSONConverter.class)
    private List<Map<String, Object>> projects;

    @Convert(converter = JSONConverter.class)
    private List<Map<String, Object>> certificates;

}

package com.ptit.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPattern {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false, columnDefinition = "VARCHAR(5000)")
    private String content;

    private Boolean patternCms;
}

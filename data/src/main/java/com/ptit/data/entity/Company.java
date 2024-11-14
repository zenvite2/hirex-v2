package com.ptit.data.entity;

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
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employerId;

    private String companyName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    private String website;

    private String logo;

    private String address;

    private Long city;

    private Long district;

    private Long scale;
}

package com.ptit.data.entity;

import com.ptit.data.base.Auditable;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class Notification extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long toUserId;

    private String title;

    private String content;

    private boolean read = false;

    private Long sendTo; // (0: tất cả, 1: employee, 2 employer)
}

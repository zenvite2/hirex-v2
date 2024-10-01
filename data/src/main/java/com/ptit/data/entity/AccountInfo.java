package com.ptit.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Data
@Entity(name = "account_info")
public class AccountInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "VARCHAR(40)")
    private String id;

    @Column(name = "email", columnDefinition = "VARCHAR(50)", unique = true)
    private String email;

    @Column(name = "hash_password", columnDefinition = "VARCHAR(64)")
    private String hashPassword;
//
//    @Column(name = "login_type", columnDefinition = "SMALLINT")
//    private LoginTypeEnum loginType = LoginTypeEnum.EMAIL;

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "TIMESTAMP", updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
    private ZonedDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = ZonedDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = ZonedDateTime.now();
    }
}

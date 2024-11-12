package com.ptit.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ptit.data.base.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reply extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private Long userId;

    private String username;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    @JsonIgnore
    private Comment parentComment;
}

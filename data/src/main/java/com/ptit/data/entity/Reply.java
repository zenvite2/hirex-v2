package com.ptit.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ptit.data.base.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "comment_id")
    @JsonIgnore
    private Comment parentComment;

    @ManyToOne
    @JoinColumn(name = "parent_reply_id")
    @JsonIgnore
    private Reply parentReply;

    @OneToMany(mappedBy = "parentReply", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Reply> childReplies = new ArrayList<>();
}

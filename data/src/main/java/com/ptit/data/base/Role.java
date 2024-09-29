package com.ptit.data.base;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    private String id;

    private String name;

    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";
    public static final String EMPLOYEE = "EMPLOYEE";
    public static final String EMPLOYER = "EMPLOYER";
}

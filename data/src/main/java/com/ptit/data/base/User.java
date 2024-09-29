package com.ptit.data.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "user") // Collection name in MongoDB
public class User extends Auditable implements UserDetails {
    private static final long serialVersionUID = 1L;

    @Id
    private String id; // Use String for MongoDB ID

    private String fullName;

    private String address;

    @JsonIgnore
    private String password;

    private String phoneNumber;

    private String dateOfBirth;

    private String mail;

    private String gender;

    private String avtUrl; // Updated to camelCase to follow Java naming conventions

    @DBRef // Reference to the Role document
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        if (role != null) { // Check if role is not null before accessing it
            authorityList.add(new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase()));
        }
        return authorityList;
    }

    @Override
    public String getUsername() {
        return phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Return true, or implement your logic
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Return true, or implement your logic
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Return true, or implement your logic
    }

    @Override
    public boolean isEnabled() {
        return true; // Return true, or implement your logic
    }
}

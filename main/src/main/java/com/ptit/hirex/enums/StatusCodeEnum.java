package com.ptit.hirex.enums;

public enum StatusCodeEnum {
    // AUTHENTICATION
    AUTH0012("Sign-in successful"),
    AUTH0013("Invalid credentials"),
    AUTH0014("Authentication failed"),
    AUTH0015("Invalid refresh token"),
    AUTH0016("User not found"),
    AUTH0017("Failed to save token"),
    AUTH0018("Error during token refresh"),

    // SIGNUP
    AUTH0019("Email already exists"),
    AUTH0020("Role not found"),
    AUTH0021("Failed to save user"),
    AUTH0022("User created successfully"),
    AUTH0023("Error during user creation"),
    AUTH0024("MPassword and confirmation password do not match."),

    //EMPLOYEE
    EMPLOYEE0000("Create Employee failed"),
    EMPLOYEE1000("Create Employee success"),

    //EMPLOYER
    EMPLOYER0000("Create Employer failed"),
    EMPLOYER1000("Create Employer success"),

    //UPLOADFILE
    UPLOADFILE0000("UPLOADFILE0000"), // Upload file pdf failed
    UPLOADFILE0001("UPLOADFILE0001"), // Upload file avatar failed
    UPLOADFILE0002("UPLOADFILE0002"); // Upload file cover image failed

    public final String value;

    StatusCodeEnum(String value) {
        this.value = value;
    }
}



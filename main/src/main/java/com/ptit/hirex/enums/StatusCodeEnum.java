package com.ptit.hirex.enums;

public enum StatusCodeEnum {
    // AUTHENTICATION
    AUTH0012("Sign-in successful"),
    AUTH0013("Invalid credentials"),
    AUTH0014("Authentication failed"),
    AUTH0016("User not found"),
    AUTH0017("Failed to save token"),

    // SIGNUP
    AUTH0019("User name already exists"),
    AUTH0020("Role not found"),
    AUTH0021("Failed to save user"),
    AUTH0022("User created successfully"),
    AUTH0023("Error during user creation"),
    AUTH0024("MPassword and confirmation password do not match."),

    //EMPLOYEE
    EMPLOYEE0000("Create Employee failed"),
    EMPLOYEE1000("Create Employee success"),
    EMPLOYEE1001("Get Employee success"),
    EMPLOYEE0001("Get Employee failed"),
    EMPLOYEE4000("Not found Employee"),
    EMPLOYEE1002("Update Employee success"),
    EMPLOYEE0002("Update Employee failed"),

    //EMPLOYER
    EMPLOYER0000("Create Employer failed"),
    EMPLOYER1000("Create Employer success"),
    EMPLOYER1001("Get Employer success"),
    EMPLOYER0001("Get Employer failed"),
    EMPLOYER4000("Not found Employer"),
    EMPLOYER1002("Update Employer success"),
    EMPLOYER0002("Update Employer failed"),

    //UPLOADFILE
    UPLOADFILE0000("UPLOADFILE0000"), // Upload file pdf failed
    UPLOADFILE0001("UPLOADFILE0001"), // Upload file avatar failed
    UPLOADFILE0002("UPLOADFILE0002"), // Upload file cover image failed

    //EXPERIENCE
    EXPERIENCE1000("EXPERIENCE1000"), //Create experience success
    EXPERIENCE0000("EXPERIENCE0000"), //Create experience failed
    EXPERIENCE1001("EXPERIENCE1001"), //Update experience success
    EXPERIENCE0001("EXPERIENCE0001"), //Update experience failed
    EXPERIENCE1002("EXPERIENCE1002"), //Get experience success
    EXPERIENCE0002("EXPERIENCE0002"), //Get experience failed
    EXPERIENCE1003("EXPERIENCE1003"), //Delete experience success
    EXPERIENCE0003("EXPERIENCE0003"), //Delete experience failed
    EXPERIENCE4000("EXPERIENCE4000"), //Not found experience

    //EDUCATION
    EDUCATION1000("EDUCATION1000"), //Create education success
    EDUCATION0000("EDUCATION0000"), //Create education failed
    EDUCATION1001("EDUCATION1001"), //Update education success
    EDUCATION0001("EDUCATION0001"), //Update education failed
    EDUCATION1002("EDUCATION1002"), //Get education success
    EDUCATION0002("EDUCATION0002"), //Get education failed
    EDUCATION1003("EDUCATION1003"), //Delete education success
    EDUCATION0003("EDUCATION0003"), //Delete education failed
    EDUCATION4000("EDUCATION4000"), //Not found education

    //SKILL
    SKILL1000("SKILL1000"), //Create skill success
    SKILL0000("SKILL0000"), //Create skill failed
    SKILL1001("SKILL1001"), //Update skill success
    SKILL0001("SKILL0001"), //Update skill failed
    SKILL1002("SKILL1002"), //Get skill success
    SKILL0002("SKILL0002"), //Get skill failed
    SKILL1003("SKILL1003"), //Delete skill success
    SKILL0003("SKILL0003"), //Delete skill failed
    SKILL4000("SKILL4000"), //Not found skill

    //CAREER
    CAREER1000("CAREER1000"), //Create career goal success
    CAREER0000("CAREER0000"), //Create career goal failed
    CAREER1001("CAREER1001"), //Update career goal success
    CAREER0001("CAREER0001"), //Update career goal failed
    CAREER1002("CAREER1002"), //Get career goal success
    CAREER0002("CAREER0002"), //Get career goal failed
    CAREER1003("CAREER1003"), //Delete career goal success
    CAREER0003("CAREER0003"), //Delete career goal failed

    //JOB
    JOB0000("Create job failed"),
    JOB1000("Create job success"),
    JOB1001("Get job success"),
    JOB0001("Get job failed"),
    JOB4000("Not found job"),
    JOB1002("Update job success"),
    JOB0002("Update job failed"),
    JOB1003("Delete job success"),
    JOB0003("Delete job failed"),

    //APPLICATION
    APPLICATION1000("Create application success"),
    APPLICATION0001("Create application failed"),

    //CATEGORY
    CITY1000("CITY1000"), //Autofill city success
    CITY0000("CITY0000"), //Autofill city failed
    DISTRICT1000("DISTRICT1000"), //Autofill district success
    DISTRICT0000("DISTRICT0000"), //Autofill district failed
    JOBTYPE1000("JOBTYPE1000"), //Autofill job type success
    JOBTYPE0000("JOBTYPE0000"), //Autofill job type failed
    TECH1000("TECH1000"), //Autofill tech success
    TECH0000("TECH0000"), //Autofill tech failed
    YEAREXPERIENCE1000("YEAREXPERIENCE1000"), //Autofill tech success
    YEAREXPERIENCE0000("YEAREXPERIENCE0000"), //Autofill tech failed
    POSITION1000("POSITION1000"), //Autofill position success
    POSITION0000("POSITION0000"), //Autofill position failed
    CONTRACTTYPE1000("CONTRACTTYPE1000"), //Autofill contract type success
    CONTRACTTYPE0000("CONTRACTTYPE0000"), //Autofill contract type failed
    COMPANY1000("COMPANY1000"), //Autofill company success
    COMPANY0000("COMPANY0000"), //Autofill company failed
    SALARY1000("SALARY1000"), //Autofill salary success
    SALARY0000("SALARY0000"); //Autofill salary failed

    public final String value;

    StatusCodeEnum(String value) {
        this.value = value;
    }
}



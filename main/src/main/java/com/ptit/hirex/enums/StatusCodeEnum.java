package com.ptit.hirex.enums;

public enum StatusCodeEnum {
    // AUTHENTICATION
    AUTH0012("Sign-in successful"),
    AUTH0013("Invalid credentials"),
    AUTH0014("Authentication failed"),
    AUTH0016("User not found"),
    AUTH0017("Failed to save token"),
    AUTH0025("User not found"),
    AUTH0026("Auth password old incorrect"),
    AUTH0027("Auth password same as old"),
    AUTH0028("Auth password change error"),
    AUTH0029("Auth reset password success"),
    AUTH0030("Auth reset password failed"),

    //EMAIL
    EMAIL0000("Email not found"),

    // SIGNUP
    AUTH0019("User name already exists"),
    AUTH0020("Role not found"),
    AUTH0021("Failed to save user"),
    AUTH0022("User created successfully"),
    AUTH0023("Error during user creation"),
    AUTH0024("Password and confirmation password do not match."),


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

    //SAVE JOB
    SAVE1000("SAVE1000"), //Save job success
    SAVE0000("SAVE0000"), //Save job failed
    SAVE1001("SAVE1001"), //Update save job success
    SAVE0001("SAVE0001"), //Update save job failed
    SAVE1002("SAVE1002"), //Get save job success
    SAVE0002("SAVE0002"), //Get save job failed
    SAVE1003("SAVE1003"), //Delete save job success
    SAVE0003("SAVE0003"), //Delete save job failed

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

    //COMPANY
    COMPANY1001("Get company success"),
    COMPANY0001("Get company failed"),
    COMPANY1002("Get company success"),
    COMPANY0003("Get company failed"),
    COMPANY0002("Not found company"),

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
    SALARY0000("SALARY0000"), //Autofill salary failed
    INDUSTRY1000("INDUSTRY1000"), //Autofill industry success
    INDUSTRY0000("INDUSTRY0000"), //Autofill industry failed
    EDUCATIONLEVEL1000("EDUCATIONLEVEL1000"), //Autofill education level success
    EDUCATIONLEVEL0000("EDUCATIONLEVEL0000"), //Autofill education level failed

    // USER-INFO
    USERINFO1000("USERINFO1000"),
    USERINFO0000("USERINFO0000"),
    // MESSAGE
    MESSAGE1000("MESSAGE1000"),
    MESSAGE0000("MESSAGE0000"),

    // COMMENT
    COMMENT1000("Comment created successfully"),
    COMMENT1001("Comments retrieved successfully"),
    COMMENT0000("Failed to get comment"),
    COMMENT0001("Failed to get comments"),

    // REPLY
    REPLY1000("Reply created successfully"),
    REPLY0000("Failed to create reply"),

    // NOTIFICATION
    NOTIFICATION1000("Mark notification success"),
    NOTIFICATION0000("Mark notification failed"),
    NOTIFICATION1001("Get notification success"),
    NOTIFICATION0001("Get notification failed"),
    NOTIFICATION1002("Get notification success"),
    NOTIFICATION0002("Get notification failed"),

    //SAVE JOB
    FOLLOW1000("FOLLOW1000"), //Create follow company success
    FOLLOW0000("FOLLOW0000"), //Create follow company failed
    FOLLOW1001("FOLLOW1001"), //Update follow company success
    FOLLOW0001("FOLLOW0001"), //Update follow company failed
    FOLLOW1002("FOLLOW1002"), //Get follow company success
    FOLLOW0002("FOLLOW0002"), //Get follow company failed
    FOLLOW1003("FOLLOW1003"), //Delete follow company success
    FOLLOW0003("FOLLOW0003"), //Delete follow company failed

    // RESUME
    RESUME1000("Create resume success"),
    RESUME0000("Create resume failed"),
    RESUME1001("Get resume success"),
    RESUME0001("Get resume failed"),
    RESUME1002("Delete resume success"),
    RESUME0002("Delete resume failed");

    public final String value;

    StatusCodeEnum(String value) {
        this.value = value;
    }
}



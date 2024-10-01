package com.ptit.hirex.enums;

public enum StatusCodeEnum {
    //EXCEPTION
    EXCEPTION("EXCEPTION"), // Exception
    EXCEPTION0400("EXCEPTION0400"), // Bad request
    EXCEPTION0404("EXCEPTION0404"), // Not found
    EXCEPTION0503("EXCEPTION0503"), // Http message not readable
    EXCEPTION0504("EXCEPTION0504"), // Missing servlet request parameter
    EXCEPTION0505("EXCEPTION0505"), // Access Denied/Not have permission

    //ACCOUNT
    ACCOUNT1000("ACCOUNT1000"), // Get my account information successfully
    ACCOUNT0000("ACCOUNT0000"), // Get my account information failed
    ACCOUNT0001("ACCOUNT0001"), // Get account information failed
    ACCOUNT0002("ACCOUNT0002"), // Change password success
    ACCOUNT0003("ACCOUNT0003"), // Change password failed
    ACCOUNT0004("ACCOUNT0004"), // Incorrect login information
    ACCOUNT0005("ACCOUNT0005"), // New password cannot be the same as current password

    //COMPANY
    COMPANY0008("COMPANY0008"), // Get representative but company not found
    COMPANY0009("COMPANY0009"), // Get representative but representative not found
    COMPANY0010("COMPANY0010"), // Get representative successfully
    COMPANY0011("COMPANY0011"), // Get representative failed
    COMPANY0012("COMPANY0012"), // Update representative but company not found
    COMPANY0013("COMPANY0013"), // Update representative but representative not found
    COMPANY0014("COMPANY0014"), // Update representative successfully
    COMPANY0015("COMPANY0015"), // Update representative failed
    COMPANY0016("COMPANY0016"), // Get potential but company not found
    COMPANY0017("COMPANY0017"), // Get potential but potential not found
    COMPANY0018("COMPANY0018"), // Get potential successfully
    COMPANY0019("COMPANY0019"), // Get potential failed
    COMPANY0020("COMPANY0020"), // Update potential but company not found
    COMPANY0021("COMPANY0021"), // Update potential but potential not found
    COMPANY0022("COMPANY0022"), // Update potential successfully
    COMPANY0023("COMPANY0023"), // Update potential failed
    COMPANY00024("COMPANY00024"), // Get privacy settings information successfully
    COMPANY0002("COMPANY0002"), // No company information available
    COMPANY0003("COMPANY0003"), // Failed to get privacy settings information
    COMPANY0004("COMPANY0004"), // Update company privacy settings but company not found
    COMPANY0005("COMPANY0005"), // Create company privacy settings
    COMPANY0006("COMPANY0006"), // Update company privacy settings successfully
    COMPANY0007("COMPANY0007"), // Update company privacy settings failed

    //AUTH
    AUTH1000("AUTH1000"), // Login google successfully
    AUTH1001("AUTH1001"), // Login facebook successfully
    AUTH1003("AUTH1003"), // Login successfully
    AUTH1004("AUTH1004"), // Register successfully
    AUTH1005("AUTH1005"), // Get access token successfully
    AUTH0000("AUTH0000"), // Login google failed because verify google authorization code failed
    AUTH0001("AUTH0001"), // Login google failed because verify google id token failed
    AUTH0002("AUTH0002"), // Login google failed because get account of social id failed
    AUTH0003("AUTH0003"), // Login google failed because email login google has been used
    AUTH0004("AUTH0004"), // Login google failed because create new account failed
    AUTH0005("AUTH0005"), // Login facebook failed because verify facebook access token failed
    AUTH0006("AUTH0006"), // Login facebook failed because get user facebook failed
    AUTH0007("AUTH0007"), // Login facebook failed because get account of social id failed
    AUTH0008("AUTH0008"), // Login facebook failed because email login facebook has been used
    AUTH0009("AUTH0009"), // Login facebook failed because create new account failed
    AUTH0010("AUTH0010"), // Login failed
    AUTH0011("AUTH0011"), // Incorrect login information
    AUTH0012("AUTH0012"), // Account already exists
    AUTH0013("AUTH0013"), // Register failed
    AUTH0014("AUTH0014"), // Account already exists
    AUTH0015("AUTH0015"), // Get access token failed

    //COMPANY
    COMPANY0000("COMPANY0000"), // Get company not found
    COMPANY1001("COMPANY1001"), // Update company success
    COMPANY0001("COMPANY0001"), // Update company failed
    COMPANY1002("COMPANY1002"), // Get company success

    //UPLOADFILE
    UPLOADFILE0000("UPLOADFILE0000"), // Upload file pdf failed
    UPLOADFILE0001("UPLOADFILE0001"), // Upload file avatar failed
    UPLOADFILE0002("UPLOADFILE0002"), // Upload file cover image failed

    //POTENTIAL PARTNER
    PARTNER0000("PARTNER0000"), // Get potential partner failed
    PARTNER1000("PARTNER1000"), // Get potential partner success
    PARTNER2001("PARTNER2001"), // Update potential partner success
    PARTNER0002("PARTNER0002"), // Update potential partner failed
    PARTNER0004("PARTNER0004"), // Potential partner not found
    PARTNER1001("PARTNER1001"), // Update potential partner success
    PARTNER0001("PARTNER0001"), // Update potential partner failed


    //RESET PASSWORD
    RESETPASS1000("RESETPASS1000"), // Verify OTP password reset success
    RESETPASS1001("RESETPASS1001"), // Reset password success
    RESETPASS0000("RESETPASS0000"), // This email does not exist for reset password
    RESETPASS0001("RESETPASS0001"), // Type login not support for reset password
    RESETPASS0002("RESETPASS0002"), // Verify OTP password reset failed
    RESETPASS0003("RESETPASS0003"), // This email does not exist for reset password
    RESETPASS0004("RESETPASS0004"), // Type login not support for reset password
    RESETPASS0005("RESETPASS0005"), // Save reset token to redis failed
    RESETPASS0006("RESETPASS0006"), // Update account password failed
    RESETPASS0007("RESETPASS0007"), // Get account id from token failed
    RESETPASS0008("RESETPASS0008"), // Get account info failed for reset password

    //RESET LANGUAGE
    RESETLA0000("RESETLA0000"), // Change language success
    RESETLA0001("RESETLA0001"), // Change language failed

    // OTP
    OTP1000("OTP1000"), // Send otp to request register successfully
    OTP1001("OTP1001"), // Send otp to request reset password successfully
    OTP0000("OTP0000"), // Send otp to request register failed
    OTP0001("OTP0001"), // Enter incorrect OTP
    OTP0002("OTP0002"), // Wrong otp reset password
    OTP0003("OTP0003"), // Send otp to request reset password failed

    //COMPANY SEARCH
    SEARCHCOMPANY1000("SEARCHCOMPANY1000"),
    SEARCHCOMPANY0000("SEARCHCOMPANY0000"),

    //COMPANY SUGGEST
    SUGGESTCOMPANY1000("SUGGESTCOMPANY1200"),
    SUGGESTCOMPANY0000("SUGGESTCOMPANY0200"),

    //REPRESENTATIVE
    REPRESENTATIVE0000("REPRESENTATIVE0000"), // Get representative failed
    REPRESENTATIVE1000("REPRESENTATIVE1000"), // Get representative success
    REPRESENTATIVE1001("REPRESENTATIVE1001"), // Update representative success
    REPRESENTATIVE0001("REPRESENTATIVE0001"), // Update representative failed
    REPRESENTATIVE0002("REPRESENTATIVE0002"), // Representative not found

    // CATEGORY
    SCALE1200("SCALE1200"), // Autofill scale category successfully
    SCALE0200("SCALE0200"), // Autofill scale category failed
    ACTIVITYAREA1200("ACTIVITYAREA1200"), // Autofill activity area successfully
    ACTIVITYAREA0200("ACTIVITYAREA0200"), // Autofill activity area failed
    ANNUALREVENUE1200("ANNUALREVENUE1200"), // Autofill annual revenue successfully
    ANNUALREVENUE0200("ANNUALREVENUE0200"), // Autofill annual revenue failed
    COUNTRY1200("COUNTRY1200"), // Autofill country successfully
    COUNTRY0200("COUNTRY0200"), // Autofill country failed
    PROVINCE1200("PROVINCE1200"), // Autofill province successfully
    PROVINCE0200("PROVINCE0200"), // Autofill province failed

    //COMPANY RELATIONSHIP CATEGORY
    RELATIONSHIPCATEGORY1000("RELATIONSHIPCATEGORY1000"), // Create company relationship category success
    RELATIONSHIPCATEGORY0000("RELATIONSHIPCATEGORY0000"), // Create company relationship category failed
    RELATIONSHIPCATEGORY1001("RELATIONSHIPCATEGORY1001"), // Get list company relationship category success
    RELATIONSHIPCATEGORY0001("RELATIONSHIPCATEGORY0001"), // Get list company relationship category failed
    RELATIONSHIPCATEGORY1002("RELATIONSHIPCATEGORY1002"), // Update company relationship category success
    RELATIONSHIPCATEGORY0002("RELATIONSHIPCATEGORY0002"), // Update company relationship category failed
    RELATIONSHIPCATEGORY0003("RELATIONSHIPCATEGORY0003"), // Not found company relationship category
    RELATIONSHIPCATEGORY1003("RELATIONSHIPCATEGORY1003"), // Delete company relationship category success
    RELATIONSHIPCATEGORY0004("RELATIONSHIPCATEGORY0004"), // Delete company relationship category failed
    RELATIONSHIPCATEGORY1004("RELATIONSHIPCATEGORY1004"), // Delete and transfer company relationship category success
    RELATIONSHIPCATEGORY0005("RELATIONSHIPCATEGORY0005"), // Delete and transfer company relationship category failed

    //COMPANY RELATIONSHIP
    RELATIONSHIP1000("RELATIONSHIP1000"), // Create company relationship success
    RELATIONSHIP0000("RELATIONSHIP0000"), // Create company relationship failed
    RELATIONSHIP0001("RELATIONSHIP0001"), // Not found company relationship
    RELATIONSHIP1001("RELATIONSHIP1001"), // Update company relationship success
    RELATIONSHIP0002("RELATIONSHIP0002"), // Update company relationship failed
    RELATIONSHIP1002("RELATIONSHIP1002"), // Delete company relationship success
    RELATIONSHIP0003("RELATIONSHIP0003"), // Delete company relationship failed
    RELATIONSHIP1003("RELATIONSHIP1003"), // Get list company relationship success
    RELATIONSHIP0004("RELATIONSHIP0004"), // Get list company relationship failed

    //THANKS LETTER
    THANKSLETTER0000("THANKSLETTER0000"), // Get thanks letter success
    THANKSLETTER1000("THANKSLETTER0001"), // Get thanks letter failed
    THANKSLETTER1001("THANKSLETTER1001"), // Create thanks letter success
    THANKSLETTER0001("THANKSLETTER0001"), // Create thanks letter failed
    THANKSLETTER0002("THANKSLETTER0002"); // Thanks letter not found

    public final String value;

    StatusCodeEnum(String i) {
        value = i;
    }
}


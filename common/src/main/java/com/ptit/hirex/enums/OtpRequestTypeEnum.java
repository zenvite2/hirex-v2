package com.ptit.hirex.enums;

public enum OtpRequestTypeEnum {
    REGISTER(0), RESET_PASSWORD(1);

    public final int value;

    OtpRequestTypeEnum(int i) {
        value = i;
    }
}

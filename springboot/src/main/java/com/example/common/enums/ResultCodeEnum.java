package com.example.common.enums;

public enum ResultCodeEnum {

    SUCCESS("200", "succeed"),
    PARAM_ERROR("400", "The parameter is abnormal"),
    TOKEN_INVALID_ERROR("401", "Invalid tokens"),
    TOKEN_CHECK_ERROR("401", "Token verification failed, please log in again"),
    PARAM_LOST_ERROR("4001", "Parameters are missing"),

    SYSTEM_ERROR("500", "The system is abnormal"),
    USER_EXIST_ERROR("5001", "The username already exists"),
    USER_NOT_LOGIN("5002", "The user is not logged in"),
    USER_ACCOUNT_ERROR("5003", "The account number or password is incorrect"),
    USER_NOT_EXIST_ERROR("5004", "The user does not exist"),
    PARAM_PASSWORD_ERROR("5005", "The original password was entered incorrectly"),
    ;

    public String code;
    public String msg;

    ResultCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

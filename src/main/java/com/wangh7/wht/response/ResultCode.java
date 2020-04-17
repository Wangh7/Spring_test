package com.wangh7.wht.response;

public enum ResultCode {
    SUCCESS(200),
    FAIL(400),
    UNAUTHORIZED(401),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500),

    BALANCENOTENOUGH(801);

    public int code;
    ResultCode(int code){
        this.code = code;
    }
}

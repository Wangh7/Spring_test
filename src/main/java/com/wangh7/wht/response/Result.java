package com.wangh7.wht.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Data
public class Result {
    private int code;
    private String message;
    private Object result;

    public Result(int code,String message,Object data){
        this.code = code;
        this.message = message;
        this.result = data;
    }
}

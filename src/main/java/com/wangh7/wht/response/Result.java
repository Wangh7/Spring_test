package com.wangh7.wht.response;

import lombok.Getter;
import lombok.Setter;

public class Result {
    @Getter @Setter
    private int code;
    public Result(int code){
        this.code = code;
    }
}

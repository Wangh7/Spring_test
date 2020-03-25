package com.wangh7.wht.entity;

import lombok.Data;

@Data
public class LoginUser {
    String username;
    String password;
    boolean remember;
}

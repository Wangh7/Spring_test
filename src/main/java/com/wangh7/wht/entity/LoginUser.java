package com.wangh7.wht.entity;

import lombok.Data;

@Data
public class LoginUser {
    int userId;
    String username;
    String password;
    boolean remember;
    String nickname;
}

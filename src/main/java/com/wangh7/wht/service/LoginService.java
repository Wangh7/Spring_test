package com.wangh7.wht.service;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    public boolean isLogin() {
        Subject currentUser = SecurityUtils.getSubject();
        if(currentUser.isAuthenticated()){
            System.out.println("登录");
            return true;
        } else {
            System.out.println("未登录");
            return false;
        }
    }
}

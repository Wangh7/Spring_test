package com.wangh7.wht.service;

import com.wangh7.wht.pojo.User;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    public User hashPass(User user, String plainPass){
        //生成slat
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        //hash迭代次数
        int times = 2;
        String encodedPassword = new SimpleHash("md5", plainPass, salt, times).toString();
        user.setSalt(salt);
        user.setPassword(encodedPassword);
        return user;
    }
}
package com.wangh7.wht.service;

import com.wangh7.wht.pojo.User;
import com.wangh7.wht.utils.DesUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PasswordService {

    public User hashPass(User user, String plainPass) {
        //生成slat
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        //hash迭代次数
        int times = 2;
        String encodedPassword = new SimpleHash("md5", plainPass, salt, times).toString();
        user.setSalt(salt);
        user.setPassword(encodedPassword);
        return user;
    }


    public String DES(String cardNum, String data, String mode) throws Exception {
        DesUtils desUtils = new DesUtils(cardNum);
        switch (mode) {
            case "encode":
                return desUtils.encode(data);
            case "decode":
                return desUtils.decode(data);
            default:
                return null;
        }
    }

    public String getRandomPass() {
        String num = "0123456789";
        String word = "qwertyuiopasdfghjklzxcvbnm";
        String wordBig = "QWERTYUIOPASDFGHJKLZXCVBNM";
        String symbol = "!@#$%^&*()-=_+";
        String sum = num + word + symbol + wordBig + symbol + num;
        String password = "";
        int passlength = 16;
        for (int i = 0; i < passlength; i++) {
            Random random = new Random();
            int a = random.nextInt(sum.length());
            char s = sum.charAt(a);
            password = password + s;
        }
        return password;
    }
}

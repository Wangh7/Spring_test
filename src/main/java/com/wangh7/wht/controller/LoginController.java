package com.wangh7.wht.controller;

import com.wangh7.wht.pojo.User;
import com.wangh7.wht.response.Result;
import com.wangh7.wht.response.ResultFactory;
import com.wangh7.wht.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.List;


@RestController
public class LoginController {

    @Autowired
    UserService userService;

//    @CrossOrigin
//    @PostMapping(value = "api/login")
//    @ResponseBody
//    public Result login(@RequestBody User requestUser) {
//        String username = requestUser.getUsername();
//        username = HtmlUtils.htmlEscape(username);
//
//        User user = userService.get(username,requestUser.getPassword());
//        if (null == user){
//            return new Result(400,null,null);
//        } else {
//            return new Result(200,null,null);
//        }
//    }

    @CrossOrigin
    @PostMapping(value = "api/login")
    @ResponseBody
    public Result login(@RequestBody User requestUser) {
        String username = requestUser.getUsername();
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username,requestUser.getPassword());
        try {
            subject.login(usernamePasswordToken);
            return ResultFactory.buildSuccessResult(usernamePasswordToken);
        } catch (AuthenticationException e){
            String message = "用户名或密码错误";
            return ResultFactory.buildFailResult(message);
        }
    }

    @CrossOrigin
    @GetMapping("api/logout")
    public Result logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        String message = "成功登出";
        return ResultFactory.buildSuccessResult(message);
    }

    @ResponseBody
    @GetMapping(value = "api/authentication")
    public String authentication(){
        return "身份认证成功";
    }
    @CrossOrigin
    @GetMapping(value = "api/login/select")
    public List<User> list() throws Exception {
        return userService.list();
    }

    @CrossOrigin
    @PostMapping(value = "api/register")
    @ResponseBody
    public Result register(@RequestBody User user){
        String username = user.getUsername();
        String password = user.getPassword();
        username = HtmlUtils.htmlEscape(username);//特殊字符转义
        user.setUsername(username);

        boolean exist = userService.isExist(username);
        if(exist){
            String message = "用户名已被使用";
            return ResultFactory.buildFailResult(message);
        }

        //生成slat
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        //hash迭代次数
        int times = 2;
        String encodedPassword = new SimpleHash("md5",password,salt,times).toString();
        //存储用户信息
        user.setSalt(salt);
        user.setPassword(encodedPassword);
        userService.add(user);

        return ResultFactory.buildSuccessResult(user);
    }
}

package com.wangh7.wht.controller;

import com.wangh7.wht.entity.LoginUser;
import com.wangh7.wht.pojo.User;
import com.wangh7.wht.response.Result;
import com.wangh7.wht.response.ResultFactory;
import com.wangh7.wht.service.PasswordService;
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
    @Autowired
    PasswordService passwordService;

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
//    public Result login(@RequestBody User requestUser, boolean remember) {
//        String username = requestUser.getUsername();
//        Subject subject = SecurityUtils.getSubject();
//        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, requestUser.getPassword());
//        System.out.println("传参remember" + remember);
////        if(remember){
////            usernamePasswordToken.setRememberMe(true);
////        }
//        try {
//            subject.login(usernamePasswordToken);
//            return ResultFactory.buildSuccessResult(usernamePasswordToken);
//        } catch (AuthenticationException e) {
//            String message = "用户名或密码错误";
//            return ResultFactory.buildFailResult(message);
//        }
//    }
    public Result login(@RequestBody LoginUser loginUser) {
        String username = loginUser.getUsername();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, loginUser.getPassword());
//        System.out.println("传参remember" + loginUser.isRemember());
        if(loginUser.isRemember()){
            usernamePasswordToken.setRememberMe(true);
        }
        try {
            subject.login(usernamePasswordToken);
            loginUser.setNickname(userService.findByUsername(username).getNickname());
            return ResultFactory.buildSuccessResult(loginUser);
//            return ResultFactory.buildSuccessResult(usernamePasswordToken);
        } catch (AuthenticationException e) {
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
    public String authentication() {
        return "身份认证成功";
    }


    @CrossOrigin
    @PostMapping(value = "api/register")
    @ResponseBody
    public Result register(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        username = HtmlUtils.htmlEscape(username);//特殊字符转义
        user.setUsername(username);

        boolean exist = userService.isExist(username);
        if (exist) {
            String message = "用户名已被使用";
            return ResultFactory.buildFailResult(message);
        }
        user = passwordService.hashPass(user, password);
        //存储用户信息
        userService.add(user);

        return ResultFactory.buildSuccessResult(user);
    }
}

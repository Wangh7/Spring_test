package com.wangh7.wht.controller;

import com.wangh7.wht.entity.LoginUser;
import com.wangh7.wht.pojo.Price;
import com.wangh7.wht.pojo.Role;
import com.wangh7.wht.pojo.User;
import com.wangh7.wht.response.Result;
import com.wangh7.wht.response.ResultFactory;
import com.wangh7.wht.service.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;


@RestController
public class LoginController {

    @Autowired
    UserService userService;
    @Autowired
    PasswordService passwordService;
    @Autowired
    RoleService roleService;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    PriceService priceService;

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
            loginUser.setUserId(userService.findByUsername(username).getId());
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
        user.setEnabled(true);
        //存储用户信息
        userService.add(user);
        //分配角色
        Role role;
        List<Role> roles = new ArrayList<>();
        role = roleService.findById(3); //普通用户
        roles.add(role);
        userRoleService.saveRoleChanges(user.getId(),roles);
        //创建余额
        Money money = Money.of(CurrencyUnit.of("CNY"),0);
        Price price = new Price();
        price.setUserId(user.getId());
        price.setMoney(money);
        priceService.addOrUpdate(price);
        return ResultFactory.buildSuccessResult(user);
    }
}

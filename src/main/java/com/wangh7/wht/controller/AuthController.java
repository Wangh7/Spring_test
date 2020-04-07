package com.wangh7.wht.controller;


import com.wangh7.wht.response.Result;
import com.wangh7.wht.response.ResultFactory;
import com.wangh7.wht.service.LoginService;
import com.wangh7.wht.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    MenuService menuService;
    @Autowired
    LoginService loginService;

    @GetMapping(value = "/api/authentication")
    public Result authentication() {
        if(loginService.isLogin()){
            return ResultFactory.buildSuccessResult("已登录");
        } else {
            return ResultFactory.buildFailResult("未登录");
        }
    }

    @CrossOrigin
    @GetMapping(value = "/api/auth")
    public Result auth(String url) {
        if(menuService.menuPerm(url)){
            System.out.println("界面："+url+"验证成功");
            return ResultFactory.buildSuccessResult("界面验证成功");
        } else {
            System.out.println("界面："+url+"无权限");
            return ResultFactory.buildUnAuthResult("界面验证失败");
        }
    }
}

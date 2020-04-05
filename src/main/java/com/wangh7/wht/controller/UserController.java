package com.wangh7.wht.controller;


import com.wangh7.wht.entity.ChangePass;
import com.wangh7.wht.pojo.User;
import com.wangh7.wht.response.Result;
import com.wangh7.wht.response.ResultFactory;
import com.wangh7.wht.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    SecurityManager securityManager;

    @CrossOrigin
    @GetMapping(value = "/api/user")
    public List<User> list() throws Exception {
        return userService.list();
    }

    @CrossOrigin
    @GetMapping(value = "/api/user/single")
    public User singleUser() {
        Subject subject = SecurityUtils.getSubject();
        return userService.singleUser(subject.getPrincipal().toString());
    }

    @CrossOrigin
    @PostMapping(value = "/api/user/single")
    public Result editSingleUser(@RequestBody User user) {
        if(userService.editUser(user)) {
            return ResultFactory.buildSuccessResult("修改成功");
        } else {
            return ResultFactory.buildFailResult("修改失败");
        }
    }

    @CrossOrigin
    @PostMapping(value = "/api/user/single/pass")
    public Result changePass(@RequestBody ChangePass changePass) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(subject.getPrincipal().toString(), changePass.getOldPass());
        try {
            securityManager.authenticate(token);
        } catch (Exception e){
            return ResultFactory.buildFailResult("修改失败，原密码错误");
        }
        //生成slat
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        //hash迭代次数
        int times = 2;
        String encodedPassword = new SimpleHash("md5", changePass.getNewPass(), salt, times).toString();
        //存储用户信息
        User user = new User();
        user.setUsername(subject.getPrincipal().toString());
        user.setSalt(salt);
        user.setPassword(encodedPassword);
        if(userService.changePass(user)){
            return ResultFactory.buildSuccessResult("密码修改成功");
        } else {
            return ResultFactory.buildFailResult("密码修改失败");
        }
    }

    @CrossOrigin
    @PostMapping(value = "/api/user")
    public Result editUser(@RequestBody User user) {
        if(userService.editUser(user)) {
            return ResultFactory.buildSuccessResult("修改成功");
        } else {
            return ResultFactory.buildFailResult("修改失败");
        }
    }

    @CrossOrigin
    @PutMapping(value = "/api/user/status")
    public Result updateUserStatus(@RequestBody User user) {
        if(userService.updateUserStatus(user)) {
            return ResultFactory.buildSuccessResult("用户状态更新成功");
        } else {
            return ResultFactory.buildFailResult("用户状态更新失败");
        }
    }
}

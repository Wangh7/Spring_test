package com.wangh7.wht.controller;


import com.wangh7.wht.pojo.User;
import com.wangh7.wht.response.Result;
import com.wangh7.wht.response.ResultFactory;
import com.wangh7.wht.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @CrossOrigin
    @GetMapping(value = "/api/user")
    public List<User> list() throws Exception {
        return userService.list();
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
}

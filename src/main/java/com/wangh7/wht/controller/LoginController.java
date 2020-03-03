package com.wangh7.wht.controller;

import com.wangh7.wht.pojo.User;
import com.wangh7.wht.response.Result;
import com.wangh7.wht.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.List;


@RestController
public class LoginController {

    @Autowired
    UserService userService;

    @CrossOrigin
    @PostMapping(value = "api/login")
    @ResponseBody
    public Result login(@RequestBody User requestUser) {
        String username = requestUser.getUsername();
        username = HtmlUtils.htmlEscape(username);

        User user = userService.get(username,requestUser.getPassword());
        if (null == user){
            return new Result(400);
        } else {
            return new Result(200);
        }
    }
    @CrossOrigin
    @GetMapping(value = "api/login/select")
    public List<User> list() throws Exception {
        return userService.list();
    }


}

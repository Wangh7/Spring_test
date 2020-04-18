package com.wangh7.wht.controller;


import com.wangh7.wht.pojo.Test;
import com.wangh7.wht.service.PasswordService;
import com.wangh7.wht.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {
    @Autowired
    TestService testService;
    @CrossOrigin
    @GetMapping(value = "api/test")
    public void list() throws Exception {
        testService.list();
    }
}

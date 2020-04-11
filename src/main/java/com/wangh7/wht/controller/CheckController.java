package com.wangh7.wht.controller;


import com.wangh7.wht.entity.ItemCheck;
import com.wangh7.wht.pojo.ItemSell;
import com.wangh7.wht.response.Result;
import com.wangh7.wht.response.ResultFactory;
import com.wangh7.wht.service.ItemSellService;
import com.wangh7.wht.service.PasswordService;
import com.wangh7.wht.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CheckController {
    @Autowired
    ItemSellService itemSellService;
    @Autowired
    UserService userService;
    @Autowired
    PasswordService passwordService;

    @CrossOrigin
    @GetMapping(value = "/api/check")
    public List<ItemSell> getAllItemSell(@RequestParam String status) {
        return itemSellService.getStatusList(status);
    }

    @CrossOrigin
    @GetMapping(value = "/api/check/own")
    public List<ItemSell> getItemSell() {
        int user_id = userService.findByUsername(SecurityUtils.getSubject().getPrincipal().toString()).getId();
        return itemSellService.getOwnList(user_id);
    }

    @CrossOrigin
    @PostMapping(value = "/api/check/success")
    public Result checkSuccess(@RequestBody ItemCheck itemCheck) throws Exception{
        if(itemSellService.checkSuccess(itemCheck)) {
            return ResultFactory.buildSuccessResult("审查成功");
        } else {
            return ResultFactory.buildFailResult("审查失败");
        }
    }
    @CrossOrigin
    @PostMapping(value = "/api/check/fail")
    public Result checkFail(@RequestBody ItemCheck itemCheck) {
        if(itemSellService.checkFail(itemCheck)) {
            return ResultFactory.buildSuccessResult("打回成功");
        } else {
            return ResultFactory.buildFailResult("打回失败");
        }
    }

    @CrossOrigin
    @GetMapping(value = "/api/check/pass")
    public String getDecodePass(@RequestParam String pass) throws Exception{
        return passwordService.DES(pass,"decode");
    }
}

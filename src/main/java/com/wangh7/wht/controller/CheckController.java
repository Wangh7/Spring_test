package com.wangh7.wht.controller;


import com.wangh7.wht.entity.CheckIndex;
import com.wangh7.wht.entity.ItemCheck;
import com.wangh7.wht.pojo.ItemSell;
import com.wangh7.wht.pojo.User;
import com.wangh7.wht.response.Result;
import com.wangh7.wht.response.ResultFactory;
import com.wangh7.wht.service.ItemBuyService;
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
    ItemBuyService itemBuyService;
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
    @GetMapping(value = "/api/check/buyer")
    public User getBuyerInfo(@RequestParam int item_id) {
        return userService.singleUser(itemBuyService.getUserId(item_id));
    }

    @CrossOrigin
    @GetMapping(value = "/api/check/seller")
    public User getSellerInfo(@RequestParam int item_id) {
        return userService.singleUser(itemSellService.getUserId(item_id));
    }
    @CrossOrigin
    @PostMapping(value = "/api/check/success")
    public Result checkSuccess(@RequestBody ItemCheck itemCheck) throws Exception {
        if (itemSellService.checkSuccess(itemCheck)) {
            return ResultFactory.buildSuccessResult("审查成功");
        } else {
            return ResultFactory.buildFailResult("审查失败");
        }
    }

    @CrossOrigin
    @PostMapping(value = "/api/check/fail")
    public Result checkFail(@RequestBody ItemCheck itemCheck) {
        if (itemSellService.checkFail(itemCheck)) {
            return ResultFactory.buildSuccessResult("打回成功");
        } else {
            return ResultFactory.buildFailResult("打回失败");
        }
    }

    @CrossOrigin
    @GetMapping(value = "/api/check/oldPass")
    public String getDecodePass(@RequestParam int itemId, @RequestParam String pass) throws Exception {
        String cardNum = itemSellService.getItemById(itemId).getCardNum();
        return passwordService.DES(cardNum, pass, "decode");
    }

    @CrossOrigin
    @GetMapping(value = "/api/check/newPass")
    public String getNewPass() throws Exception {
        return passwordService.getRandomPass();
    }

    @CrossOrigin
    @PostMapping(value = "/api/check/entity")
    public Result confirmItem(@RequestBody ItemCheck itemCheck) {
        if(itemSellService.checkEntity(itemCheck)) {
            return ResultFactory.buildSuccessResult("审核成功");
        } else {
            return ResultFactory.buildFailResult("审核失败");
        }
    }

    @CrossOrigin
    @PostMapping(value = "/api/check/entity/success")
    public Result successItem(@RequestBody ItemCheck itemCheck) {
        if(itemSellService.checkEntitySuccess(itemCheck)) {
            return ResultFactory.buildSuccessResult("审核成功");
        } else {
            return ResultFactory.buildFailResult("审核失败");
        }
    }

    @CrossOrigin
    @PostMapping(value = "/api/check/entity/fail")
    public Result failItem(@RequestBody ItemCheck itemCheck) {
        if(itemSellService.checkEntityFail(itemCheck)) {
            return ResultFactory.buildSuccessResult("审核成功");
        } else {
            return ResultFactory.buildFailResult("审核失败");
        }
    }

    @CrossOrigin
    @GetMapping(value = "/api/check/index")
    public CheckIndex getCheckIndex() {
        int user_id = userService.findByUsername(SecurityUtils.getSubject().getPrincipal().toString()).getId();
        return itemSellService.getCheckIndex(user_id);
    }
}

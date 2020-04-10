package com.wangh7.wht.controller;

import com.wangh7.wht.pojo.ItemType;
import com.wangh7.wht.response.Result;
import com.wangh7.wht.response.ResultFactory;
import com.wangh7.wht.service.ItemTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DiscountController {
    @Autowired
    ItemTypeService itemTypeService;

    @CrossOrigin
    @GetMapping(value = "/api/discount")
    public List<ItemType> list() {
        return itemTypeService.list();
    }

    @CrossOrigin
    @PostMapping(value = "/api/discount/type")
    public Result setItemDiscount(@RequestBody ItemType itemType) {
        if(itemTypeService.setItemDiscount(itemType)) {
            return ResultFactory.buildSuccessResult("更新成功");
        } else {
            return ResultFactory.buildFailResult("更新失败");
        }
    }
}

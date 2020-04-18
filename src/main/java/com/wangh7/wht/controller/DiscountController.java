package com.wangh7.wht.controller;

import com.wangh7.wht.pojo.DiscountTime;
import com.wangh7.wht.pojo.ItemType;
import com.wangh7.wht.response.Result;
import com.wangh7.wht.response.ResultFactory;
import com.wangh7.wht.service.DiscountTimeService;
import com.wangh7.wht.service.ItemTypeService;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DiscountController {
    @Autowired
    ItemTypeService itemTypeService;
    @Autowired
    DiscountTimeService discountTimeService;

    @CrossOrigin
    @GetMapping(value = "/api/discount")
    public List<ItemType> itemTypeList() {
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

    @CrossOrigin
    @GetMapping(value = "/api/discount/time")
    public List<DiscountTime> discountTimeList() {
        return discountTimeService.list();
    }

    @CrossOrigin
    @PutMapping(value = "/api/discount/time/status")
    public Result updateUserStatus(@RequestBody DiscountTime discountTime) {
        if(discountTimeService.discountTimeStatus(discountTime)) {
            return ResultFactory.buildSuccessResult("折扣状态更新成功");
        } else {
            return ResultFactory.buildFailResult("折扣状态更新失败");
        }
    }
}

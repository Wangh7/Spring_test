package com.wangh7.wht.controller;

import com.wangh7.wht.pojo.Price;
import com.wangh7.wht.response.Result;
import com.wangh7.wht.response.ResultFactory;
import com.wangh7.wht.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PriceController {
    @Autowired
    PriceService priceService;

    @CrossOrigin
    @GetMapping(value = "/api/price")
    public Result list() {
        return ResultFactory.buildSuccessResult(priceService.list());
    }

    @CrossOrigin
    @PostMapping(value = "/api/price")
    public Result addOrUpdate(@RequestBody Price price) {
        try {
            priceService.addOrUpdate(price);
        } catch (IllegalArgumentException e) {
            return ResultFactory.buildSuccessResult("");
        }
        return ResultFactory.buildFailResult("error");
    }
}

package com.wangh7.wht.controller;

import com.wangh7.wht.response.Result;
import com.wangh7.wht.response.ResultFactory;
import com.wangh7.wht.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuController {
    @Autowired
    MenuService menuService;

    @GetMapping("/api/menu")
    public Result menu() {
        return ResultFactory.buildSuccessResult(menuService.getMenusByCurrentUser());
    }

    @GetMapping("/api/role/menu")
    public Result listAllMenus() {
        return ResultFactory.buildSuccessResult(menuService.getMenusByRoleId(1));
    }
}

package com.wangh7.wht.controller;

import com.wangh7.wht.response.Result;
import com.wangh7.wht.response.ResultFactory;
import com.wangh7.wht.service.MenuService;
import com.wangh7.wht.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

@RestController
public class MenuController {
    @Autowired
    MenuService menuService;
    @Autowired
    RoleMenuService roleMenuService;

    @GetMapping("/api/menu")
    public Result menu() {
        return ResultFactory.buildSuccessResult(menuService.getMenusByCurrentUser());
    }

    @GetMapping("/api/role/menu")
    public Result listAllMenus() {
        return ResultFactory.buildSuccessResult(menuService.list());
    }


    @PutMapping("/api/role/menu")
    public Result updateRoleMenu(@RequestParam int role_id, @RequestBody LinkedHashMap menusIds) {
        if(roleMenuService.updateRoleMenu(role_id,menusIds)) {
            return ResultFactory.buildSuccessResult("更新成功");
        } else {
            return ResultFactory.buildFailResult("参数错误，更新失败");
        }
    }
}

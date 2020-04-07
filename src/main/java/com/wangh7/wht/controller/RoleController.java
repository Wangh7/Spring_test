package com.wangh7.wht.controller;


import com.wangh7.wht.pojo.Role;
import com.wangh7.wht.pojo.User;
import com.wangh7.wht.response.Result;
import com.wangh7.wht.response.ResultFactory;
import com.wangh7.wht.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

@RestController
public class RoleController {
    @Autowired
    RoleService roleService;
    @Autowired
    PermissionService permissionService;
    @Autowired
    RolePermissionService rolePermissionService;
    @Autowired
    RoleMenuService roleMenuService;

    @Autowired
    UserService userService;

    @GetMapping("/api/role")
    public Result listRoles() {
        return ResultFactory.buildSuccessResult(roleService.list());
    }

    @PutMapping("/api/role/status")
    public Result updateRoleStatus(@RequestBody Role requestRole) {
        Role role = roleService.updateRoleStatus(requestRole);
        String message = "用户" + role.getNameZh() + "状态更新成功";
        return ResultFactory.buildSuccessResult(message);
    }

//    @PutMapping("/api/role")
//    public Result editRole(@RequestBody Role requestRole) {
//        roleService.addOrUpdate(requestRole);
//        rolePermissionService.savePermChanges(requestRole.getId(), requestRole.getPermissions());
//        String message = "修改角色信息成功";
//        return ResultFactory.buildSuccessResult(message);
//    }

    @PostMapping("/api/role")
    public Result editRole(@RequestBody Role requestRole) {
        if (roleService.editRole(requestRole)) {
            return ResultFactory.buildSuccessResult("修改角色成功");
        } else {
            return ResultFactory.buildFailResult("参数错误，修改角色失败");
        }
    }

    @GetMapping("/api/role/perm")
    public Result listPerms() {
        return ResultFactory.buildSuccessResult(permissionService.list());
    }

}

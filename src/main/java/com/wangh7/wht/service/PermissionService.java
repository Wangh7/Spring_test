package com.wangh7.wht.service;

import com.wangh7.wht.dao.PermissionDAO;
import com.wangh7.wht.pojo.Permission;
import com.wangh7.wht.pojo.Role;
import com.wangh7.wht.pojo.RolePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PermissionService {
    @Autowired
    PermissionDAO permissionDAO;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    RoleService roleService;
    @Autowired
    UserService userService;
    @Autowired
    RolePermissionService rolePermissionService;

    public Permission findById(int id){
        return permissionDAO.findById(id);
    }

    public List<Permission> list() {
        return permissionDAO.findAll();
    }

    public boolean needFilter(String requestAPI) {
        List<Permission> permissions = permissionDAO.findAll();
        for (Permission permission : permissions) {
            // 前缀进行匹配，拥有父权限就拥有子权限
            if(requestAPI.startsWith(permission.getUrl())){
                return true;
            }
        }
        return false;
    }

    public List<Permission> listPermissionByRoleId(int role_id) {
        List<RolePermission> rolePermissions = rolePermissionService.findAllByRoleId(role_id);
        List<Permission> permissions = new ArrayList<>();
        for (RolePermission rolePermission : rolePermissions) {
            permissions.add(permissionDAO.findById(rolePermission.getPermissionId()));
        }
        return permissions;
    }

    public Set<String> listPermissionURLsByUser(String username) {
        List<Role> roles = roleService.listRolesByUser(username);
        Set<String> URLs = new HashSet<>();

        for(Role role : roles) {
            List<RolePermission> rolePermissions = rolePermissionService.findAllByRoleId(role.getId());
            for(RolePermission rolePermission : rolePermissions) {
                URLs.add(permissionDAO.findById(rolePermission.getPermissionId()).getUrl());
            }
        }
        return URLs;
    }
}

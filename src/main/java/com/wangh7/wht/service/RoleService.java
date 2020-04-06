package com.wangh7.wht.service;

import com.wangh7.wht.dao.RoleDAO;
import com.wangh7.wht.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService {
    @Autowired
    RoleDAO roleDAO;
    @Autowired
    UserService userService;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    PermissionService permissionService;
    @Autowired
    RolePermissionService rolePermissionService;
    @Autowired
    MenuService menuService;

    public Role findById(int id){
        return roleDAO.findById(id);
    }

    public void addOrUpdate(Role role){
        roleDAO.save(role);
    }

    public List<Role> list() {
        List<Role> roles = roleDAO.findAll();
        List<Permission> permissions;
        List<Menu> menus;
        for(Role role : roles) {
            permissions = permissionService.listPermissionByRoleId(role.getId());
            menus = menuService.getMenusByRoleId(role.getId());
            role.setPermissions(permissions);
            role.setMenus(menus);
        }
        return roles;
    }

    public List<Role> listRolesByUser(String username) {
        int user_id = userService.findByUsername(username).getId();
        List<Role> roles = new ArrayList<>();
        List<UserRole> userRoles = userRoleService.listAllByUserId(user_id);
        for(UserRole userRole : userRoles) {
            roles.add(roleDAO.findById(userRole.getRoleId()));
        }
        return roles;
    }

    public Role updateRoleStatus(Role role) {
        Role roleInDB = roleDAO.findById(role.getId());
        roleInDB.setEnabled(role.isEnabled());
        return roleDAO.save(roleInDB);
    }

    public boolean editRole(Role role) {
        Role roleInDB = roleDAO.findById(role.getId());
        roleInDB.setName(role.getName());
        roleInDB.setNameZh(role.getNameZh());
        try {
            roleDAO.save(roleInDB);
            rolePermissionService.savePermChanges(roleInDB.getId(), role.getPermissions());
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}

package com.wangh7.wht.service;

import com.wangh7.wht.dao.RolePermissionDAO;
import com.wangh7.wht.pojo.Permission;
import com.wangh7.wht.pojo.RolePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RolePermissionService {
    @Autowired
    RolePermissionDAO rolePermissionDAO;

    List<RolePermission> findAllByRoleId(int role_id) {
        return rolePermissionDAO.findAllByRoleId(role_id);
    }

    @Transactional
    public void savePermChanges(int role_id, List<Permission> permissions) {
        rolePermissionDAO.deleteAllByRoleId(role_id);
        for (Permission permission :permissions) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(role_id);
            rolePermission.setPermissionId(permission.getId());
            rolePermissionDAO.save(rolePermission);
        }
    }
}

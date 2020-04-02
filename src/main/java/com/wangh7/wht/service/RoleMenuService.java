package com.wangh7.wht.service;

import com.wangh7.wht.dao.RoleMenuDAO;
import com.wangh7.wht.pojo.RoleMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;

@Service
public class RoleMenuService {
    @Autowired
    RoleMenuDAO roleMenuDAO;

    public List<RoleMenu> findAllByRoleId(int role_id){
        return roleMenuDAO.findAllByRoleId(role_id);
    }

    @Modifying
    @Transactional
    public void deleteAllByRoleId(int role_id){
        roleMenuDAO.deleteAllByRoleId(role_id);
    }

    public void save(RoleMenu roleMenu){
        roleMenuDAO.save(roleMenu);
    }

    @Modifying
    @Transactional
    public boolean updateRoleMenu(int role_id, LinkedHashMap menusIds) {
        try {
            deleteAllByRoleId(role_id);
            for (Object value : menusIds.values()) {
                for (int menu_id : (List<Integer>) value) {
                    RoleMenu roleMenu = new RoleMenu();
                    roleMenu.setRoleId(role_id);
                    roleMenu.setMenuId(menu_id);
                    roleMenuDAO.save(roleMenu);
                }
            }
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}

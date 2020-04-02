package com.wangh7.wht.service;

import com.wangh7.wht.dao.RoleDAO;
import com.wangh7.wht.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    RoleDAO roleDAO;
    @Autowired
    UserService userService;

    public Role findById(int id){
        return roleDAO.findById(id);
    }

    public void addOrUpdate(Role role){
        roleDAO.save(role);
    }


}

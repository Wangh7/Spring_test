package com.wangh7.wht.service;

import com.wangh7.wht.dao.UserRoleDAO;
import com.wangh7.wht.pojo.Role;
import com.wangh7.wht.pojo.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserRoleService {
    @Autowired
    UserRoleDAO userRoleDAO;
    public List<UserRole> listAllByUserId(int user_id) {
        return userRoleDAO.findAllByUserId(user_id);
    }

    @Transactional
    public void saveRoleChanges(int user_id, List<Role> roles) {
        userRoleDAO.deleteAllByUserId(user_id);
        for (Role role : roles) {
            UserRole userRole = new UserRole();
            userRole.setUserId(user_id);
            userRole.setRoleId(role.getId());
            userRoleDAO.save(userRole);
        }
    }
}

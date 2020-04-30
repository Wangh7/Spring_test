package com.wangh7.wht.service;

import com.wangh7.wht.dao.UserDAO;
import com.wangh7.wht.pojo.Role;
import com.wangh7.wht.pojo.User;
import com.wangh7.wht.pojo.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;
    @Autowired
    RoleService roleService;
    @Autowired
    UserRoleService userRoleService;

    public boolean isExist(String username){
        User user = getByName(username);
        return null!=user;
    }

    public User getByName(String username){
        return userDAO.findByUsername(username);
    }

    public User get(String username,String password){
        return userDAO.getByUsernameAndPassword(username, password);
    }

    public void add(User user){
        userDAO.save(user);
    }

    public List<User> list() { //获取所有用户
//        Sort sort = Sort.by(Sort.Direction.DESC, "id");
//        return userDAO.findAll(sort);
        List<User> users = userDAO.list();
        List<Role> roles;
        for (User user : users) {
            roles = roleService.listRolesByUser(user.getUsername());
            user.setRoles(roles);
        }
        return users;

    }

    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public User singleUser(String username) {
        return userDAO.singleUser(username);
    }
    public User singleUser(int user_id) {
        return userDAO.singleUser(user_id);
    }

    public void addOrUpdate(User user) {
        userDAO.save(user);
    }

    public void deleteById(int id) {
        userDAO.deleteById(id);
    }

    public boolean editUser(User user) {
        User userInDB = userDAO.findByUsername(user.getUsername());
        userInDB.setUsername(user.getUsername());
        userInDB.setNickname(user.getNickname());
        userInDB.setPhone(user.getPhone());
        userInDB.setAddress(user.getAddress());
        try {
            userDAO.save(userInDB);
            userRoleService.saveRoleChanges(userInDB.getId(), user.getRoles());
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
    public boolean editUserSingle(User user) {
        User userInDB = userDAO.findByUsername(user.getUsername());
        userInDB.setUsername(user.getUsername());
        userInDB.setNickname(user.getNickname());
        userInDB.setPhone(user.getPhone());
        userInDB.setAddress(user.getAddress());
        try {
            userDAO.save(userInDB);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public boolean changePass(User user) {
        User userInDB = userDAO.findByUsername(user.getUsername());
        userInDB.setPassword(user.getPassword());
        userInDB.setSalt(user.getSalt());
        try {
            userDAO.save(userInDB);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public boolean updateUserStatus(User user) {
        User userInDB = userDAO.findByUsername(user.getUsername());
        userInDB.setEnabled(user.isEnabled());
        try {
            userDAO.save(userInDB);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}

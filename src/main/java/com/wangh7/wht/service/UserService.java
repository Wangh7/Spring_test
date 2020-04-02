package com.wangh7.wht.service;

import com.wangh7.wht.dao.UserDAO;
import com.wangh7.wht.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;

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
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        //return itemDAO.findAll(Sort.by(Sort.Direction.DESC, "item_id"));
        return userDAO.findAll(sort);

    }

    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }
}

package com.wangh7.wht.dao;

import com.wangh7.wht.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserDAO extends JpaRepository<User,Integer> {
    User findByUsername(String username);
    User getByUsernameAndPassword(String username,String password);

    @Query(value = "select new User(u.id,u.username,u.nickname,u.phone,u.enabled) from User u")
    List<User> list();

    @Query(value = "select new User(u.id,u.username,u.nickname,u.phone) from User u where u.username = ?1")
    User singleUser(String username);
}

package com.wangh7.wht.dao;

import com.wangh7.wht.pojo.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleDAO extends JpaRepository<UserRole, Integer> {
    List<UserRole> findAllByUserId(int user_id);
    void deleteAllByUserId(int user_id);
}

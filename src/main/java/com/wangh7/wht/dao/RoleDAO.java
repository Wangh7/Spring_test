package com.wangh7.wht.dao;

import com.wangh7.wht.pojo.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDAO extends JpaRepository<Role,Integer> {
    Role findById(int id);
}

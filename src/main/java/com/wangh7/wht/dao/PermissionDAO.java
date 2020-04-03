package com.wangh7.wht.dao;

import com.wangh7.wht.pojo.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionDAO extends JpaRepository<Permission,Integer> {
    Permission findById(int id);
}

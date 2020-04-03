package com.wangh7.wht.dao;

import com.wangh7.wht.pojo.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolePermissionDAO extends JpaRepository<RolePermission,Integer> {
    List<RolePermission> findAllByRoleId(int role_id);
    void deleteAllByRoleId(int role_id);
}

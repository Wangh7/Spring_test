package com.wangh7.wht.dao;

import com.wangh7.wht.pojo.RoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleMenuDAO extends JpaRepository<RoleMenu,Integer> {
    List<RoleMenu> findAllByRoleId(int roleId);
    void deleteAllByRoleId(int roleId);
}

package com.wangh7.wht.dao;

import com.wangh7.wht.pojo.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuDAO extends JpaRepository<Menu,Integer> {
    Menu findById(int id);
    List<Menu> findAllByParentId(int parentId);
}

package com.wangh7.wht.dao;

import com.wangh7.wht.pojo.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemDAO extends JpaRepository<Item, Integer> {

}

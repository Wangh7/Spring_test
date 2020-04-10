package com.wangh7.wht.dao;

import com.wangh7.wht.pojo.ItemStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemStockDAO extends JpaRepository<ItemStock,Integer> {
}

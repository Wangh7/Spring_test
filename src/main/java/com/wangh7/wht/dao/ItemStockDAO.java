package com.wangh7.wht.dao;

import com.wangh7.wht.pojo.ItemStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemStockDAO extends JpaRepository<ItemStock, Integer> {
    List<ItemStock> findAllByItemType_TypeCodeAndStatus(String typeCode,String status);
    List<ItemStock> findAllByStatus(String status);
    ItemStock findByItemId(int item_id);
}

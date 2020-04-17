package com.wangh7.wht.dao;

import com.wangh7.wht.pojo.ItemStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemStockDAO extends JpaRepository<ItemStock, Integer> {
    List<ItemStock> findAllByItemType_TypeCode(String typeCode);
    ItemStock findByItemId(int item_id);
}

package com.wangh7.wht.dao;

import com.wangh7.wht.pojo.ItemSell;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemSellDAO extends JpaRepository<ItemSell, Integer> {
    List<ItemSell> findAllByUserId(int user_id);
    List<ItemSell> findAllByManagerId(int manager_id);

    List<ItemSell> findAllByStatus(String status);

    ItemSell findByItemId(int item_id);
    List<ItemSell> findAllByUserIdAndStatus(int user_id,String status);
}

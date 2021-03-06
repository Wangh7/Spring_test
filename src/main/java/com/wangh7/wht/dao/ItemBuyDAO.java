package com.wangh7.wht.dao;

import com.wangh7.wht.pojo.ItemBuy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemBuyDAO extends JpaRepository<ItemBuy, Integer> {
    List<ItemBuy> findAllByUserId(int user_id);

    ItemBuy findByUserIdAndItemStock_ItemId(int user_id, int item_id);

    List<ItemBuy> findAllByUserIdAndStatus(int user_id, String status);
    List<ItemBuy> findAllByUserIdAndStatusOrStatus(int user_id, String status1, String status2);
    List<ItemBuy> findAllByUserIdAndStatusNot(int user_id, String status);
    ItemBuy findByItemStock_ItemIdAndStatus(int item_id, String status);
}

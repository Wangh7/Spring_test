package com.wangh7.wht.dao;

import com.wangh7.wht.entity.HotSell;
import com.wangh7.wht.pojo.ItemStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemStockDAO extends JpaRepository<ItemStock, Integer> {
    List<ItemStock> findAllByItemType_TypeCodeAndStatus(String typeCode, String status);

    List<ItemStock> findAllByStatus(String status);

    ItemStock findByItemId(int item_id);

    @Query(value = "select new com.wangh7.wht.entity.HotSell(a.itemType.typeId ,a.itemType.typeName ,a.itemType.typeCode ,count(a.itemId)) from ItemStock a where a.status = 'Y' group by a.itemType.typeId order by count(a.itemId) desc")
    List<HotSell> getHotSell();
}

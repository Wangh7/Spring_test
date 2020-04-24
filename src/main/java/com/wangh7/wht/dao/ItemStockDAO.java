package com.wangh7.wht.dao;

import com.wangh7.wht.entity.HotSell;
import com.wangh7.wht.pojo.ItemStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemStockDAO extends JpaRepository<ItemStock, Integer> {
    List<ItemStock> findAllByItemType_TypeCodeAndStatusAndDueTimeGreaterThan(String typeCode, String status, long nowTime);

    List<ItemStock> findAllByStatusAndDueTimeGreaterThan(String status, long nowTime);

    ItemStock findByItemId(int item_id);

    @Query(value = "select new com.wangh7.wht.entity.HotSell(a.itemType.typeId ,a.itemType.typeName ,a.itemType.typeCode ,count(a.itemId)) from ItemStock a where a.status = 'Y' group by a.itemType.typeId order by count(a.itemId) desc")
    List<HotSell> getHotSell();

    @Query(value = "select new com.wangh7.wht.entity.HotSell(a.itemType.typeId, a.itemType.typeName, a.itemType.typeCode) from ItemStock a where a.status = 'N' group by a.itemType.typeId having(count(a.itemType.typeId)>=1)")
    List<HotSell> getRecentItem();
}

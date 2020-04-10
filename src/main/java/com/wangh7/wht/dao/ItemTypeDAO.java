package com.wangh7.wht.dao;

import com.wangh7.wht.pojo.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemTypeDAO extends JpaRepository<ItemType, Integer> {
    ItemType findByTypeId(int type_id);
}

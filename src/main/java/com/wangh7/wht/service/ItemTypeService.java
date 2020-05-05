package com.wangh7.wht.service;

import com.wangh7.wht.dao.ItemTypeDAO;
import com.wangh7.wht.pojo.ItemType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemTypeService {
    @Autowired
    ItemTypeDAO itemTypeDAO;

    public List<ItemType> list() {
        return itemTypeDAO.findAll();
    }

    public String getImage(int typeId) {
        ItemType itemType = itemTypeDAO.findById(typeId).orElse(null);
        String s = "/static/" + itemType.getTypeCode() + ".png";
        return s;
    }

    public boolean setItemDiscount(ItemType itemType) {
        try {
            itemTypeDAO.save(itemType);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    @Transactional
    public boolean deleteItemDiscount(ItemType itemType) {
        try {
            itemTypeDAO.deleteById(itemType.getTypeId());
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}

package com.wangh7.wht.service;

import com.wangh7.wht.dao.ItemTypeDAO;
import com.wangh7.wht.pojo.ItemType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemTypeService {
    @Autowired
    ItemTypeDAO itemTypeDAO;

    public List<ItemType> list() {
        Sort sort = Sort.by(Sort.Direction.DESC, "typeId");
        return itemTypeDAO.findAll(sort);
    }

    public String getImage(int typeId) {
        ItemType itemType = itemTypeDAO.findById(typeId).orElse(null);
        String s = "/static/" + itemType.getTypeCode() + ".png";
        return s;
    }
}

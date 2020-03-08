package com.wangh7.wht.service;


import com.wangh7.wht.dao.ItemDAO;
import com.wangh7.wht.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.util.List;

@Service
public class ItemService {
    @Autowired
    ItemDAO itemDAO;

    public List<Item> list() { //获取所有商品
        Sort sort = Sort.by(Sort.Direction.DESC, "itemId");
        //return itemDAO.findAll(Sort.by(Sort.Direction.DESC, "item_id"));
        return itemDAO.findAll(sort);

    }

    public void addOrUpdate(Item item) {
        itemDAO.save(item);
    }

    public void deleteById(int id) {
        itemDAO.deleteById(id);
    }

}

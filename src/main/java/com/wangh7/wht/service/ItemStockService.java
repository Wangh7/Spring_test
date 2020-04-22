package com.wangh7.wht.service;

import com.wangh7.wht.dao.ItemStockDAO;
import com.wangh7.wht.entity.HotSell;
import com.wangh7.wht.pojo.ItemStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemStockService {
    @Autowired
    ItemStockDAO itemStockDAO;

    public List<ItemStock> list() {
        return itemStockDAO.findAllByStatus("N");
    }

    public List<ItemStock> listByTypeCode(String typeCode) {
        return itemStockDAO.findAllByItemType_TypeCodeAndStatus(typeCode,"N");
    }
    public boolean addOrUpdate(ItemStock itemStock) {
        try {
            itemStockDAO.save(itemStock);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean delete(int item_id) {
        try {
            itemStockDAO.deleteById(item_id);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public List<HotSell> getHotSell() {
        return itemStockDAO.getHotSell();
    }

    public List<HotSell> getRecentItem() {
        return itemStockDAO.getRecentItem();
    }
}

package com.wangh7.wht.service;

import com.wangh7.wht.dao.ItemStockDAO;
import com.wangh7.wht.entity.HotSell;
import com.wangh7.wht.pojo.ItemStock;
import com.wangh7.wht.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemStockService {
    @Autowired
    ItemStockDAO itemStockDAO;

    public List<ItemStock> list() {
        DateTimeUtils dateTimeUtils = new DateTimeUtils();
        return itemStockDAO.findAllByStatusAndDueTimeGreaterThan("N", dateTimeUtils.getTimeLong());
    }

    public List<ItemStock> listByTypeCode(String typeCode) {
        DateTimeUtils dateTimeUtils = new DateTimeUtils();
        return itemStockDAO.findAllByItemType_TypeCodeAndStatusAndDueTimeGreaterThan(typeCode, "N", dateTimeUtils.getTimeLong());
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

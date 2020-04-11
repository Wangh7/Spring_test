package com.wangh7.wht.service;


import com.wangh7.wht.dao.ItemSellDAO;
import com.wangh7.wht.dao.ItemStockDAO;
import com.wangh7.wht.entity.ItemCheck;
import com.wangh7.wht.pojo.ItemSell;
import com.wangh7.wht.pojo.ItemStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.util.List;

@Service
public class ItemSellService {
    @Autowired
    ItemSellDAO itemSellDAO;
    @Autowired
    ItemStockDAO itemStockDAO;
    @Autowired
    PasswordService passwordService;

    public List<ItemSell> list() { //获取所有商品
        Sort sort = Sort.by(Sort.Direction.DESC, "itemId");
        return itemSellDAO.findAll(sort);

    }

    public List<ItemSell> getStatusList(String status) {
        return itemSellDAO.findAllByStatus(status);
    }

    public List<ItemSell> getOwnList(int manager_id) {
        return itemSellDAO.findAllByManagerId(manager_id);
    }

    public boolean addOrUpdate(ItemSell itemSell) {
        try {
            itemSell.setCardPass(passwordService.DES(itemSell.getCardPass(),"encode"));
            itemSellDAO.save(itemSell);
        } catch (Exception e) {
            return false;
        }
        return true;

    }

    public void deleteById(int id) {
        itemSellDAO.deleteById(id);
    }

    public List<ItemSell> userList(int user_id){
        return itemSellDAO.findAllByUserId(user_id);
    }

    public boolean checkSuccess(ItemCheck itemCheck) throws Exception{
        ItemSell itemSellInDB = itemSellDAO.findByItemId(itemCheck.getItemId());
        ItemStock itemStock = new ItemStock();
        itemSellInDB.setStatus("T");
        itemSellInDB.setCheckTime(itemCheck.getCheckTime());
        itemSellInDB.setManagerId(itemCheck.getManagerId());
        itemStock.setItemId(itemSellInDB.getItemId());
        itemStock.setItemType(itemSellInDB.getItemType());
        itemStock.setManagerId(itemSellInDB.getManagerId());
        itemStock.setCardNum(itemSellInDB.getCardNum());
        itemStock.setCardPass(passwordService.DES(itemCheck.getNewPassword(),"encode"));
        itemStock.setStatus("N");
        itemStock.setCreateTime(itemCheck.getCheckTime());
        try {
            itemStockDAO.save(itemStock);
            itemSellDAO.save(itemSellInDB);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public boolean checkFail(ItemCheck itemCheck) {
        ItemSell itemSellInDB = itemSellDAO.findByItemId(itemCheck.getItemId());
        itemSellInDB.setStatus("F");
        itemSellInDB.setCheckTime(itemCheck.getCheckTime());
        itemSellInDB.setManagerId(itemCheck.getManagerId());
        try {
            itemSellDAO.save(itemSellInDB);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}

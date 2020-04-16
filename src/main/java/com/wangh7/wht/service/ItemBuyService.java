package com.wangh7.wht.service;

import com.wangh7.wht.dao.ItemBuyDAO;
import com.wangh7.wht.pojo.ItemBuy;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.util.List;

@Service
public class ItemBuyService {
    @Autowired
    ItemBuyDAO itemBuyDAO;

    public List<ItemBuy> list() {
        return itemBuyDAO.findAll();
    }

    public List<ItemBuy> userBuyList(int user_id) {
        return itemBuyDAO.findAllByUserIdAndStatus(user_id,"N");
    }

    public List<ItemBuy> userBoughtList(int user_id) {
        return itemBuyDAO.findAllByUserIdAndStatus(user_id,"Y");
    }
    public void addOrUpdate(ItemBuy itemBuy) {
        itemBuyDAO.save(itemBuy);
    }

    public void deleteById(int id) {
        itemBuyDAO.deleteById(id);
    }

    public boolean userBuyItem(int user_id, List<Integer> item_ids) {
        try {
            for (int item_id : item_ids) {
                ItemBuy itemBuyInDB = itemBuyDAO.findByUserIdAndItemStock_ItemId(user_id,item_id);
                itemBuyInDB.setStatus("Y");
                itemBuyInDB.setPrice(itemBuyInDB.getItemStock().getPrice().multipliedBy(itemBuyInDB.getItemStock().getItemType().getTypeDiscountSell(),RoundingMode.HALF_UP).getAmount());
                itemBuyDAO.save(itemBuyInDB);
            }
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}

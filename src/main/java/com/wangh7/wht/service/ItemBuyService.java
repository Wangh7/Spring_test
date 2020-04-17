package com.wangh7.wht.service;

import com.wangh7.wht.dao.ItemBuyDAO;
import com.wangh7.wht.dao.ItemStockDAO;
import com.wangh7.wht.dao.ItemTimelineDAO;
import com.wangh7.wht.pojo.ItemBuy;
import com.wangh7.wht.pojo.ItemStock;
import com.wangh7.wht.pojo.ItemTimeline;
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
    @Autowired
    ItemStockDAO itemStockDAO;
    @Autowired
    ItemTimelineDAO itemTimelineDAO;
    @Autowired
    PriceService priceService;

    public List<ItemBuy> list() {
        return itemBuyDAO.findAll();
    }

    public List<ItemBuy> userBuyList(int user_id) {
        return itemBuyDAO.findAllByUserIdAndStatus(user_id, "N");
    }

    public List<ItemBuy> userBoughtList(int user_id) {
        return itemBuyDAO.findAllByUserIdAndStatusOrStatus(user_id,"Y","C");
    }

    public boolean addShopCar(int user_id,int item_id) {
        if(itemBuyDAO.findByUserIdAndItemStock_ItemId(user_id,item_id) == null) {
            ItemBuy itemBuy = new ItemBuy();
            ItemStock itemStock = new ItemStock();
            itemStock.setItemId(item_id);
            itemBuy.setUserId(user_id);
            itemBuy.setItemStock(itemStock);
            itemBuy.setCreateTime("2020-04-04 00:22:23");
            itemBuy.setStatus("N");
            itemBuyDAO.save(itemBuy);
            return true;
        }
        return false;
    }

    public void addOrUpdate(ItemBuy itemBuy) {
        itemBuyDAO.save(itemBuy);
    }

    public void deleteById(int id) {
        itemBuyDAO.deleteById(id);
    }

    public void userBuyItemPass(int user_id, int item_id) {
        ItemBuy itemBuyInDB = itemBuyDAO.findByUserIdAndItemStock_ItemId(user_id,item_id);
        if(itemBuyInDB.getStatus().equals("Y")){
            ItemTimeline itemTimeline = new ItemTimeline();
            itemTimeline.setItemId(item_id);
            itemTimeline.setStatus("B");
            itemTimeline.setTimestamp("2020-04-05 09:22:31");
            itemTimeline.setContent("用户查看密码");
            itemTimeline.setType("primary");
            itemTimeline.setIcon("el-icon-more");
            itemTimelineDAO.save(itemTimeline);
        }
        itemBuyInDB.setStatus("C");
        itemBuyDAO.save(itemBuyInDB);
    }

    public int userBuyItem(int user_id, String date, List<Integer> item_ids) {
        Money balance = priceService.single(user_id).getMoney();
        Money price = Money.of(CurrencyUnit.of("CNY"),0);
        for (int item_id : item_ids) {
            ItemBuy itemBuyInDB = itemBuyDAO.findByUserIdAndItemStock_ItemId(user_id, item_id);
            price = price.plus(itemBuyInDB.getItemStock().getPrice().multipliedBy(itemBuyInDB.getItemStock().getItemType().getTypeDiscountSell(), RoundingMode.HALF_UP));
        }
        if (balance.isLessThan(price)){
            return 3; //余额不足
        } else {
            try {
                for (int item_id : item_ids) {
                    ItemBuy itemBuyInDB = itemBuyDAO.findByUserIdAndItemStock_ItemId(user_id, item_id);
                    ItemStock itemStockInDB = itemStockDAO.findByItemId(item_id);
                    ItemTimeline itemTimeline = new ItemTimeline();

                    itemStockInDB.setStatus("Y");

                    itemTimeline.setItemId(item_id);
                    itemTimeline.setStatus("B");
                    itemTimeline.setTimestamp(date);
                    itemTimeline.setContent("用户提交订单");

                    itemBuyInDB.setStatus("Y");
                    itemBuyInDB.setPrice(itemBuyInDB.getItemStock().getPrice().multipliedBy(itemBuyInDB.getItemStock().getItemType().getTypeDiscountSell(), RoundingMode.HALF_UP).getAmount());

                    itemBuyDAO.save(itemBuyInDB);
                    itemStockDAO.save(itemStockInDB);
                    itemTimelineDAO.save(itemTimeline);

                    //扣款
                    priceService.minus(user_id,item_id,date,itemBuyInDB.getItemStock().getPrice().getAmount(),itemBuyInDB.getItemStock().getItemType().getTypeDiscountSell());

                }
            } catch (IllegalArgumentException e) {
                return 0; //异常
            }
        }
        return 1; //成功
    }
}

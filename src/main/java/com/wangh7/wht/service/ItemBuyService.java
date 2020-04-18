package com.wangh7.wht.service;

import com.wangh7.wht.dao.ItemBuyDAO;
import com.wangh7.wht.dao.ItemStockDAO;
import com.wangh7.wht.dao.ItemTimelineDAO;
import com.wangh7.wht.pojo.ItemBuy;
import com.wangh7.wht.pojo.ItemStock;
import com.wangh7.wht.pojo.ItemTimeline;
import com.wangh7.wht.utils.DateTimeUtils;
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
            DateTimeUtils dateTimeUtils = new DateTimeUtils();
            ItemBuy itemBuy = new ItemBuy();
            ItemStock itemStock = new ItemStock();
            itemStock.setItemId(item_id);
            itemBuy.setUserId(user_id);
            itemBuy.setItemStock(itemStock);
            itemBuy.setCreateTime(dateTimeUtils.getTimeLong()); //时间
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
        DateTimeUtils dateTimeUtils = new DateTimeUtils();
        if(itemBuyInDB.getStatus().equals("Y")){
            ItemTimeline itemTimeline = new ItemTimeline();
            itemTimeline.setItemId(item_id);
            itemTimeline.setStatus("B");
            itemTimeline.setTimestamp(dateTimeUtils.getTimeLong());
            itemTimeline.setContent("用户查看密码");
            itemTimeline.setType("primary");
            itemTimeline.setIcon("el-icon-more");
            itemTimelineDAO.save(itemTimeline);
        }
        itemBuyInDB.setStatus("C");
        itemBuyDAO.save(itemBuyInDB);
    }

    public int userBuyItem(int user_id, List<Integer> item_ids) {
        Money balance = priceService.single(user_id).getMoney();
        Money price = Money.of(CurrencyUnit.of("CNY"),0);
        DateTimeUtils dateTimeUtils = new DateTimeUtils();
        for (int item_id : item_ids) {
            ItemBuy itemBuyInDB = itemBuyDAO.findByUserIdAndItemStock_ItemId(user_id, item_id);
            price = price.plus(itemBuyInDB.getItemStock().getPrice().multipliedBy(itemBuyInDB.getItemStock().getItemType().getTypeDiscountSell(), RoundingMode.HALF_UP));
            if(itemBuyInDB.getStatus().equals("Y")) {
                return 2; //已被卖出
            }
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
                    itemTimeline.setTimestamp(dateTimeUtils.getTimeLong()); //时间
                    itemTimeline.setContent("用户提交订单");

                    itemBuyInDB.setStatus("Y");
                    itemBuyInDB.setFinishTime(dateTimeUtils.getTimeLong()); //时间
                    itemBuyInDB.setPrice(itemBuyInDB.getItemStock().getPrice().multipliedBy(itemBuyInDB.getItemStock().getItemType().getTypeDiscountSell(), RoundingMode.HALF_UP).getAmount());

                    itemBuyDAO.save(itemBuyInDB);
                    itemStockDAO.save(itemStockInDB);
                    itemTimelineDAO.save(itemTimeline);

                    //扣款
                    priceService.minus(user_id,item_id,itemBuyInDB.getItemStock().getPrice().getAmount(),itemBuyInDB.getItemStock().getItemType().getTypeDiscountSell());

                }
            } catch (IllegalArgumentException e) {
                return 0; //异常
            }
        }
        return 1; //成功
    }
}

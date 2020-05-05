package com.wangh7.wht.service;

import com.wangh7.wht.dao.ItemBuyDAO;
import com.wangh7.wht.dao.ItemStockDAO;
import com.wangh7.wht.entity.ItemCheck;
import com.wangh7.wht.entity.ItemIndex;
import com.wangh7.wht.pojo.DiscountTime;
import com.wangh7.wht.pojo.ItemBuy;
import com.wangh7.wht.pojo.ItemStock;
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
    PriceService priceService;
    @Autowired
    DiscountTimeService discountTimeService;
    @Autowired
    ItemSellService itemSellService;
    @Autowired
    ItemTimelineService itemTimelineService;

    public List<ItemBuy> list() {
        return itemBuyDAO.findAll();
    }

    public List<ItemBuy> userBuyList(int user_id) {
        return itemBuyDAO.findAllByUserIdAndStatus(user_id, "N");
    }

    public List<ItemBuy> userBoughtList(int user_id) {
        return itemBuyDAO.findAllByUserIdAndStatusNot(user_id, "N");
    }

    public boolean addShopCar(int user_id, int item_id) {
        if (itemBuyDAO.findByUserIdAndItemStock_ItemId(user_id, item_id) == null) {
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

    public String userBuyItemPass(int user_id, int item_id) {
        ItemBuy itemBuyInDB = itemBuyDAO.findByUserIdAndItemStock_ItemId(user_id, item_id);
        if (itemBuyInDB.getStatus().equals("Y")) {
            itemBuyInDB.setStatus("C");
            itemBuyDAO.save(itemBuyInDB);
            itemTimelineService.buyEntityItem(item_id,7);
        }
        return itemBuyInDB.getItemStock().getCardNum();
    }

    public ItemIndex getIndex(int user_id) {
        Money m1 = Money.of(CurrencyUnit.of("CNY"), 0);
        Money m2 = Money.of(CurrencyUnit.of("CNY"), 0);
        int totalBuyCar = 0;
        int totalBought = 0;
        DateTimeUtils dateTimeUtils = new DateTimeUtils();
        ItemIndex itemIndex = new ItemIndex();
        List<ItemBuy> itemBuys = itemBuyDAO.findAllByUserIdAndStatusOrStatus(user_id, "Y", "C");
        for (ItemBuy itemBuy : itemBuys) {
            m1 = m1.plus(Money.of(CurrencyUnit.of("CNY"), itemBuy.getPrice()));
            m2 = m2.plus(Money.of(CurrencyUnit.of("CNY"), itemBuy.getItemStock().getPrice().getAmount()));
            if (itemBuy.getItemStock().getDueTime() - dateTimeUtils.getTimeLong() < 1296000000 && itemBuy.getItemStock().getDueTime() - dateTimeUtils.getTimeLong() > 0) {
                totalBought++;
            }
        }
        m2 = m2.minus(m1);
        itemIndex.setTotalItem(itemBuys.size());
        itemBuys = itemBuyDAO.findAllByUserIdAndStatus(user_id, "N");
        for (ItemBuy itemBuy : itemBuys) {
            if (itemBuy.getItemStock().getDueTime() - dateTimeUtils.getTimeLong() < 1296000000 && itemBuy.getItemStock().getDueTime() - dateTimeUtils.getTimeLong() > 0) {
                totalBuyCar++;
            }
        }
        itemIndex.setTotalPrice(m2.getAmount().doubleValue());
        itemIndex.setTotalBuyCar(totalBuyCar);
        itemIndex.setTotalBought(totalBought);
        return itemIndex;
    }

//    public void buyEntityItem(int item_id, int status) {
//        ItemTimeline itemTimeline = new ItemTimeline();
//        DateTimeUtils dateTimeUtils = new DateTimeUtils();
//        itemTimeline.setItemId(item_id);
//        itemTimeline.setTimestamp(dateTimeUtils.getTimeLong()); //时间
//        itemTimeline.setStatus("B");
//        switch (status) {
//            case 1:
//                itemTimeline.setContent("等待卖家发货");break;
//            case 2:
//                itemTimeline.setContent("卖家已发货至平台");break;
//            case 3:
//                itemTimeline.setContent("平台已收货，等待审核");break;
//            case 4:
//                itemTimeline.setContent("平台审核通过，已发货");break;
//            case 5:
//                itemTimeline.setContent("买家确认收货");
//                itemTimeline.setIcon("el-icon-check");
//                itemTimeline.setType("success");break;
//            case 6:
//                itemTimeline.setContent("商品审核出现问题，交易关闭，您的资金已退回");
//                itemTimeline.setIcon("el-icon-close");
//                itemTimeline.setType("danger");break;
//        }
//        itemTimelineDAO.save(itemTimeline);
//    }

    public int getUserId(int item_id) {
        ItemBuy itemBuyInDB = itemBuyDAO.findByItemStock_ItemIdAndStatus(item_id,"W");
        return itemBuyInDB.getUserId();
    }
    public int userBuyItem(int user_id, List<Integer> item_ids) {
        Money balance = priceService.single(user_id).getMoney();
        Money price = Money.of(CurrencyUnit.of("CNY"), 0);
        DateTimeUtils dateTimeUtils = new DateTimeUtils();
        for (int item_id : item_ids) {
            ItemBuy itemBuyInDB = itemBuyDAO.findByUserIdAndItemStock_ItemId(user_id, item_id);
            ItemStock itemStock = itemStockDAO.findByItemId(item_id);
            price = price.plus(itemBuyInDB.getItemStock().getPrice().multipliedBy(itemBuyInDB.getItemStock().getItemType().getTypeDiscountSell(), RoundingMode.HALF_UP));
            if (itemStock.getStatus().equals("Y")) {
                return 2; //已被卖出
            }
            if (dateTimeUtils.getDiffDate(itemStock.getDueTime()) <= 0) {
                return 4; //已过期
            }
        }
        if (balance.isLessThan(price)) {
            return 3; //余额不足
        } else {
            try {
                List<DiscountTime> discountTimes = discountTimeService.list();
                for (int item_id : item_ids) {
                    ItemBuy itemBuyInDB = itemBuyDAO.findByUserIdAndItemStock_ItemId(user_id, item_id);
                    ItemStock itemStockInDB = itemStockDAO.findByItemId(item_id);

                    if(itemBuyInDB.getItemStock().isEntity()) { //实体卡
                        itemBuyInDB.setStatus("W"); //等待发货
                    } else { //电子卡
                        itemBuyInDB.setStatus("Y"); //购买成功
                    }
                    itemStockInDB.setStatus("Y");

                    //获取时间折扣
                    double discount = 1;
                    for (DiscountTime discountTime : discountTimes) {
                        if (dateTimeUtils.getDiffDate(itemStockInDB.getDueTime()) < discountTime.getTimeLeftday()) {
                            discount = discountTime.getDiscountSell();
                            break;
                        }
                    }


                    itemBuyInDB.setFinishTime(dateTimeUtils.getTimeLong()); //支付时间
                    itemBuyInDB.setPrice(itemBuyInDB.getItemStock().getPrice().multipliedBy(itemBuyInDB.getItemStock().getItemType().getTypeDiscountSell() * discount, RoundingMode.HALF_UP).getAmount());//实付款

                    itemBuyDAO.save(itemBuyInDB);
                    itemStockDAO.save(itemStockInDB);
                    itemTimelineService.buyEntityItem(item_id,8);

                    //扣款
                    priceService.minus(user_id, item_id, itemBuyInDB.getItemStock().getPrice().getAmount(), itemBuyInDB.getItemStock().getItemType().getTypeDiscountSell(), discount);

                    if(itemBuyInDB.getItemStock().isEntity()) { //实体卡
                        itemTimelineService.buyEntityItem(item_id,1);
                        itemSellService.buyerPayEntity(item_id);
                    }
                }
            } catch (IllegalArgumentException e) {
                return 0; //异常
            }
        }
        return 1; //成功
    }

    public boolean buyEntityItemExpress(ItemCheck itemCheck) {
        try {
            ItemBuy itemBuyInDB = itemBuyDAO.findByUserIdAndItemStock_ItemId(itemCheck.getManagerId(),itemCheck.getItemId());
            itemBuyInDB.setStatus("Y");
            itemBuyDAO.save(itemBuyInDB);
            itemTimelineService.buyEntityItem(itemCheck.getItemId(),5);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}

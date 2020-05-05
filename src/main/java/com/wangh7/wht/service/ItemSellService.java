package com.wangh7.wht.service;


import com.wangh7.wht.dao.*;
import com.wangh7.wht.entity.CheckIndex;
import com.wangh7.wht.entity.ItemCheck;
import com.wangh7.wht.entity.ItemIndex;
import com.wangh7.wht.pojo.ItemBuy;
import com.wangh7.wht.pojo.ItemSell;
import com.wangh7.wht.pojo.ItemStock;
import com.wangh7.wht.utils.DateTimeUtils;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.math.RoundingMode;
import java.util.List;

@Service
public class ItemSellService {
    @Autowired
    ItemSellDAO itemSellDAO;
    @Autowired
    ItemStockDAO itemStockDAO;
    @Autowired
    ItemBuyDAO itemBuyDAO;
    @Autowired
    ItemTypeDAO itemTypeDAO;
    @Autowired
    PasswordService passwordService;
    @Autowired
    ItemTimelineService itemTimelineService;
    @Autowired
    PriceService priceService;

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

    public ItemSell getItemById(int item_id) {
        return itemSellDAO.findByItemId(item_id);
    }

    public boolean addOrUpdate(ItemSell itemSell) {
        try {
            DateTimeUtils dateTimeUtils = new DateTimeUtils();
            if (itemSell.getCreateTime() == 0) {
                itemSell.setCreateTime(dateTimeUtils.getTimeLong());//时间
            }
            if (!itemSell.isEntity()) { //电子卡 加密存储卡密
                itemSell.setCardPass(passwordService.DES(itemSell.getCardNum(), itemSell.getCardPass(), "encode"));
            }
            itemSellDAO.save(itemSell);
            if (itemTimelineService.isExist(itemSell.getItemId())) {
                itemTimelineService.sellEntityItem(itemSell.getItemId(),9);
            } else {
                itemTimelineService.sellEntityItem(itemSell.getItemId(),10);
            }
        } catch (Exception e) {
            return false;
        }
        return true;

    }

    @Transactional
    public void deleteById(int id) {
        itemSellDAO.deleteById(id);
    }

    public List<ItemSell> userList(int user_id) {
        return itemSellDAO.findAllByUserId(user_id);
    }

    public boolean buyerPayEntity(int item_id) {
        try {
            ItemSell itemSellInDB = itemSellDAO.findByItemId(item_id);
            itemSellInDB.setStatus("N2"); //等待发货
            itemSellDAO.save(itemSellInDB);
            itemTimelineService.sellEntityItem(item_id,1);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public boolean sellEntityItemBack(ItemSell itemSell) {
        try {
            ItemSell itemSellInDB = itemSellDAO.findByItemId(itemSell.getItemId());
            itemSellInDB.setStatus("F2");//卖家确认收货
            itemSellDAO.save(itemSellInDB);
            itemTimelineService.sellEntityItem(itemSell.getItemId(),8);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public boolean sellEntityItemExpress(ItemSell itemSell) {
        try {
            ItemSell itemSellInDB = itemSellDAO.findByItemId(itemSell.getItemId());
            itemSellInDB.setCardNum(itemSell.getCardNum());
            itemSellInDB.setStatus("N3"); //已发货
            itemSellDAO.save(itemSellInDB);
            itemTimelineService.sellEntityItem(itemSell.getItemId(),2);
            itemTimelineService.buyEntityItem(itemSell.getItemId(),2);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public ItemIndex getIndex(int user_id) {
        double totalPrice = 0;
        Money total = Money.of(CurrencyUnit.of("CNY"),0);
        ItemIndex itemIndex = new ItemIndex();
        List<ItemSell> itemSells = itemSellDAO.findAllByUserIdAndStatus(user_id, "T");
        for (ItemSell itemSell : itemSells) {
            Money m = Money.of(CurrencyUnit.of("CNY"), itemSell.getPrice());
            m = m.multipliedBy(itemSell.getDiscountItem() * itemSell.getDiscountItem(), RoundingMode.HALF_UP);
            total = total.plus(m);
            totalPrice = total.getAmount().doubleValue();
        }
        itemIndex.setTotalItem(itemSells.size());
        itemIndex.setTotalPrice(totalPrice);
        itemIndex.setTotalSellN(itemSellDAO.findAllByUserIdAndStatus(user_id, "N").size());
        itemIndex.setTotalSellF(itemSellDAO.findAllByUserIdAndStatus(user_id, "F").size());
        itemIndex.setTotalSellW(itemSellDAO.findAllByUserIdAndStatus(user_id, "N2").size());
        itemIndex.setTotalSellF1(itemSellDAO.findAllByUserIdAndStatus(user_id,"F1").size());
        return itemIndex;
    }

    public CheckIndex getCheckIndex(int manager_id) {
        CheckIndex checkIndex = new CheckIndex();
        List<ItemSell> itemSells = itemSellDAO.findAllByManagerId(manager_id);
        List<ItemSell> itemSells2 = itemSellDAO.findAllByManagerId(0);
        checkIndex.setTotalCheck(itemSells.size());
        checkIndex.setWaitCheck(itemSells2.size());
        return checkIndex;
    }
    public boolean checkSuccess(ItemCheck itemCheck) throws Exception {
        ItemSell itemSellInDB = itemSellDAO.findByItemId(itemCheck.getItemId());
        ItemStock itemStock = new ItemStock();
        DateTimeUtils dateTimeUtils = new DateTimeUtils();
        if (itemSellInDB.isEntity()) { //实体卡
            itemSellInDB.setStatus("N1");
            itemTimelineService.sellEntityItem(itemCheck.getItemId(),6);
        } else { //电子卡
            itemSellInDB.setStatus("T");
            itemTimelineService.sellEntityItem(itemCheck.getItemId(),7);
            itemStock.setCardNum(itemSellInDB.getCardNum());
            itemStock.setCardPass(passwordService.DES(itemSellInDB.getCardNum(), itemCheck.getNewPassword(), "encode"));
        }
        itemSellInDB.setCheckTime(dateTimeUtils.getTimeLong()); //时间
        itemSellInDB.setManagerId(itemCheck.getManagerId());
        itemStock.setItemId(itemSellInDB.getItemId());
        itemStock.setItemType(itemSellInDB.getItemType());
        itemStock.setManagerId(itemSellInDB.getManagerId());
        itemStock.setStatus("N");
        itemStock.setCreateTime(dateTimeUtils.getTimeLong()); //时间
        itemStock.setDueTime(itemSellInDB.getDueTime());
        itemStock.setPrice(Money.of(CurrencyUnit.of("CNY"), itemCheck.getPrice()));
        itemStock.setEntity(itemSellInDB.isEntity());
        try {
            itemStockDAO.save(itemStock);
            itemSellDAO.save(itemSellInDB);
            if (!itemSellInDB.isEntity()) { //电子卡 转账
                priceService.plus(itemSellInDB.getUserId(), itemCheck.getItemId(), itemCheck.getPrice(), itemSellInDB.getDiscountItem(), itemSellInDB.getDiscountTime());
            }
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public boolean checkFail(ItemCheck itemCheck) {
        DateTimeUtils dateTimeUtils = new DateTimeUtils();
        ItemSell itemSellInDB = itemSellDAO.findByItemId(itemCheck.getItemId());
        itemSellInDB.setStatus("F");
        itemSellInDB.setCheckTime(dateTimeUtils.getTimeLong());
        itemSellInDB.setManagerId(itemCheck.getManagerId());
        try {
            itemSellDAO.save(itemSellInDB);
            itemTimelineService.sellEntityItem(itemCheck.getItemId(),11);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public boolean checkEntity(ItemCheck itemCheck) {
        try {
            ItemSell itemSellInDB = itemSellDAO.findByItemId(itemCheck.getItemId());
            itemSellInDB.setStatus("N4"); //已收货，等待审核
            itemSellDAO.save(itemSellInDB);
            itemTimelineService.sellEntityItem(itemCheck.getItemId(),3);
            itemTimelineService.buyEntityItem(itemCheck.getItemId(),3);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public boolean checkEntitySuccess(ItemCheck itemCheck) {
        try {
            ItemSell itemSellInDB = itemSellDAO.findByItemId(itemCheck.getItemId());
            ItemStock itemStockInDB = itemStockDAO.findByItemId(itemCheck.getItemId());
            itemSellInDB.setStatus("T"); //审核成功
            itemStockInDB.setCardNum(itemCheck.getExpressNumNew()); //新快递号存到stock中
            itemSellDAO.save(itemSellInDB);
            itemStockDAO.save(itemStockInDB);
            itemTimelineService.sellEntityItem(itemCheck.getItemId(),4);
            itemTimelineService.buyEntityItem(itemCheck.getItemId(),4);

            priceService.plus(itemSellInDB.getUserId(), itemSellInDB.getItemId(), itemSellInDB.getPrice(), itemSellInDB.getDiscountItem(), itemSellInDB.getDiscountTime());
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public boolean checkEntityFail(ItemCheck itemCheck) {
        try {
            ItemBuy itemBuyInDB = itemBuyDAO.findByItemStock_ItemIdAndStatus(itemCheck.getItemId(),"W");
            ItemSell itemSellInDB = itemSellDAO.findByItemId(itemCheck.getItemId());
            itemBuyInDB.setStatus("F");//交易关闭
            itemSellInDB.setStatus("F1");//审核未通过，已退回
            itemSellInDB.setCardPass(itemCheck.getExpressNumNew());//新快递号存password中
            itemBuyDAO.save(itemBuyInDB);
            itemSellDAO.save(itemSellInDB);
            itemTimelineService.sellEntityItem(itemCheck.getItemId(),5);
            itemTimelineService.buyEntityItem(itemCheck.getItemId(),6);
            //买家退款
            priceService.plus(itemBuyInDB.getUserId(), itemBuyInDB.getItemStock().getItemId(), itemBuyInDB.getPrice());
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public int getUserId(int item_id) {
        ItemSell itemSellInDB = itemSellDAO.findByItemId(item_id);
        return itemSellInDB.getUserId();
    }
}

package com.wangh7.wht.service;


import com.wangh7.wht.dao.ItemSellDAO;
import com.wangh7.wht.dao.ItemStockDAO;
import com.wangh7.wht.dao.ItemTypeDAO;
import com.wangh7.wht.entity.ItemCheck;
import com.wangh7.wht.pojo.ItemSell;
import com.wangh7.wht.pojo.ItemStock;
import com.wangh7.wht.pojo.ItemTimeline;
import com.wangh7.wht.utils.DateTimeUtils;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemSellService {
    @Autowired
    ItemSellDAO itemSellDAO;
    @Autowired
    ItemStockDAO itemStockDAO;
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

    public boolean addOrUpdate(ItemSell itemSell) {
        try {
            DateTimeUtils dateTimeUtils = new DateTimeUtils();
            if(itemSell.getCreateTime() == 0) {
                itemSell.setCreateTime(dateTimeUtils.getTimeLong());//时间
            }
            ItemTimeline itemTimeline = new ItemTimeline();
            itemTimeline.setTimestamp(dateTimeUtils.getTimeLong());
            itemTimeline.setStatus("S");
            itemSell.setCardPass(passwordService.DES(itemSell.getCardPass(),"encode"));
            itemSellDAO.save(itemSell);
            itemTimeline.setItemId(itemSell.getItemId());
            if(itemTimelineService.isExist(itemSell.getItemId())) {
                itemTimeline.setContent("用户修改商品信息");
            } else {
                itemTimeline.setContent("用户创建商品信息");
            }
            itemTimelineService.addOrUpdate(itemTimeline);
        } catch (Exception e) {
            return false;
        }
        return true;

    }

    @Transactional
    public void deleteById(int id) {
        itemSellDAO.deleteById(id);
    }

    public List<ItemSell> userList(int user_id){
        return itemSellDAO.findAllByUserId(user_id);
    }

    public boolean checkSuccess(ItemCheck itemCheck) throws Exception{
        ItemSell itemSellInDB = itemSellDAO.findByItemId(itemCheck.getItemId());
        ItemStock itemStock = new ItemStock();
        DateTimeUtils dateTimeUtils = new DateTimeUtils();
        ItemTimeline itemTimeline = new ItemTimeline();
        itemSellInDB.setStatus("T");
        itemSellInDB.setCheckTime(dateTimeUtils.getTimeLong()); //时间
        itemSellInDB.setManagerId(itemCheck.getManagerId());
        itemStock.setItemId(itemSellInDB.getItemId());
        itemStock.setItemType(itemSellInDB.getItemType());
        itemStock.setManagerId(itemSellInDB.getManagerId());
        itemStock.setCardNum(itemSellInDB.getCardNum());
        itemStock.setCardPass(passwordService.DES(itemCheck.getNewPassword(),"encode"));
        itemStock.setStatus("N");
        itemStock.setCreateTime(dateTimeUtils.getTimeLong()); //时间
        itemStock.setDueTime(itemSellInDB.getDueTime());
        itemStock.setPrice(Money.of(CurrencyUnit.of("CNY"),itemCheck.getPrice()));
        itemTimeline.setItemId(itemCheck.getItemId());
        itemTimeline.setStatus("S");
        itemTimeline.setTimestamp(dateTimeUtils.getTimeLong());
        itemTimeline.setContent("系统审核通过");
        itemTimeline.setType("success");
        itemTimeline.setIcon("el-icon-check");
        try {
            itemStockDAO.save(itemStock);
            itemSellDAO.save(itemSellInDB);
            itemTimelineService.addOrUpdate(itemTimeline);
            priceService.plus(itemSellInDB.getUserId(),itemCheck.getItemId(),itemCheck.getPrice(),itemTypeDAO.findByTypeId(itemSellInDB.getItemType().getTypeId()).getTypeDiscountBuy());
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public boolean checkFail(ItemCheck itemCheck) {
        DateTimeUtils dateTimeUtils = new DateTimeUtils();
        ItemSell itemSellInDB = itemSellDAO.findByItemId(itemCheck.getItemId());
        ItemTimeline itemTimeline = new ItemTimeline();
        itemSellInDB.setStatus("F");
        itemSellInDB.setCheckTime(dateTimeUtils.getTimeLong());
        itemSellInDB.setManagerId(itemCheck.getManagerId());
        itemTimeline.setItemId(itemCheck.getItemId());
        itemTimeline.setStatus("S");
        itemTimeline.setTimestamp(dateTimeUtils.getTimeLong());
        itemTimeline.setContent("系统审核未通过");
        itemTimeline.setType("danger");
        itemTimeline.setIcon("el-icon-close");
        try {
            itemSellDAO.save(itemSellInDB);
            itemTimelineService.addOrUpdate(itemTimeline);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}

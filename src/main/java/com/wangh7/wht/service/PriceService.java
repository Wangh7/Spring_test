package com.wangh7.wht.service;

import com.wangh7.wht.dao.PriceDAO;
import com.wangh7.wht.pojo.ItemTimeline;
import com.wangh7.wht.pojo.Price;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class PriceService {
    @Autowired
    PriceDAO priceDAO;
    @Autowired
    ItemTimelineService itemTimelineService;

    public List<Price> list() {
        return priceDAO.findAll();
    }

    public Price single(int user_id) {
        return priceDAO.findByUserId(user_id);
    }

    public boolean plus(int user_id, BigDecimal money) {
        try {
            Price price = single(user_id);
            price.setMoney(single(user_id).getMoney().plus(money));
            priceDAO.save(price);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public boolean minus(int user_id, BigDecimal money) {
        try {
            Price price = single(user_id);
            price.setMoney(single(user_id).getMoney().minus(money));
            priceDAO.save(price);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public boolean plus(int user_id, int item_id, String date, BigDecimal money, double discount) {
        try {
            ItemTimeline itemTimeline = new ItemTimeline();
            Money m = Money.of(CurrencyUnit.of("CNY"),money).multipliedBy(discount, RoundingMode.HALF_UP);
            Price price = single(user_id);
            price.setMoney(single(user_id).getMoney().plus(m));
            priceDAO.save(price);
            itemTimeline.setItemId(item_id);
            itemTimeline.setStatus("S");
            itemTimeline.setTimestamp(date);
            itemTimeline.setContent("资金已到账，收入：" + m + "元");
            itemTimeline.setType("warning");
            itemTimelineService.addOrUpdate(itemTimeline);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public boolean addOrUpdate(Price price) {
        try {
            priceDAO.save(price);
        } catch (Exception e) {
            return false;
        }
        return true;

    }
}

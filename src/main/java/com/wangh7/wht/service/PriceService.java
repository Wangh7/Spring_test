package com.wangh7.wht.service;

import com.wangh7.wht.dao.PriceDAO;
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
    public boolean plus(int user_id, int item_id, BigDecimal money) {
        try {
            Money m = Money.of(CurrencyUnit.of("CNY"), money);
            Price price = single(user_id);
            price.setMoney(single(user_id).getMoney().plus(m));
            priceDAO.save(price);
            itemTimelineService.priceTimeline(item_id,m,0,0,1);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public boolean plus(int user_id, int item_id, BigDecimal money, double discountItem, double discountTime) {
        try {
            Money m = Money.of(CurrencyUnit.of("CNY"), money).multipliedBy(discountItem * discountTime, RoundingMode.HALF_UP);
            Price price = single(user_id);
            price.setMoney(single(user_id).getMoney().plus(m));
            priceDAO.save(price);
            itemTimelineService.priceTimeline(item_id,m,discountItem,discountTime,2);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public boolean minus(int user_id, int item_id, BigDecimal money, double discountItem, double discountTime) {
        try {
            Money m = Money.of(CurrencyUnit.of("CNY"), money).multipliedBy(discountItem * discountTime, RoundingMode.HALF_UP);
            Price price = single(user_id);
            price.setMoney(single(user_id).getMoney().minus(m));
            priceDAO.save(price);
            itemTimelineService.priceTimeline(item_id,m,discountItem,discountTime,3);
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

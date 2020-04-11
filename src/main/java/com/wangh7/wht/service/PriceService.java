package com.wangh7.wht.service;

import com.wangh7.wht.dao.PriceDAO;
import com.wangh7.wht.pojo.Price;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PriceService {
    @Autowired
    PriceDAO priceDAO;

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

    public boolean addOrUpdate(Price price) {
        try {
            priceDAO.save(price);
        } catch (Exception e) {
            return false;
        }
        return true;

    }
}

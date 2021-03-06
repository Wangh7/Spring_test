package com.wangh7.wht.service;

import com.wangh7.wht.dao.DiscountTimeDAO;
import com.wangh7.wht.pojo.DiscountTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountTimeService {
    @Autowired
    DiscountTimeDAO discountTimeDAO;

    public List<DiscountTime> list() {
        return discountTimeDAO.findAll();
    }

    public List<DiscountTime> adslist() {
        return discountTimeDAO.findAllByEnabled(true);
    }

    public boolean discountTimeStatus(DiscountTime discountTime) {
        try {
            DiscountTime discountTimeInDB = discountTimeDAO.findById(discountTime.getId());
            discountTimeInDB.setEnabled(discountTime.isEnabled());
            discountTimeDAO.save(discountTimeInDB);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public boolean addOrUpdate(DiscountTime discountTime) {
        try {
            DiscountTime discountTimeInDB = discountTimeDAO.findById(discountTime.getId());
            discountTimeInDB.setDiscountSell(discountTime.getDiscountSell());
            discountTimeInDB.setDiscountBuy(discountTime.getDiscountBuy());
            discountTimeDAO.save(discountTimeInDB);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}

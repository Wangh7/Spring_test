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
}

package com.wangh7.wht.dao;

import com.wangh7.wht.pojo.Price;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceDAO extends JpaRepository<Price, Integer> {
    Price findByUserId(int user_id);
}

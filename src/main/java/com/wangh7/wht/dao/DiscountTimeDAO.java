package com.wangh7.wht.dao;

import com.wangh7.wht.pojo.DiscountTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscountTimeDAO extends JpaRepository<DiscountTime, Integer> {
    DiscountTime findById(int id);
    List<DiscountTime> findAllByEnabled(boolean enabled);
}

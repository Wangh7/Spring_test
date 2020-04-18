package com.wangh7.wht.dao;

import com.wangh7.wht.pojo.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestDAO extends JpaRepository<Test, Integer> {
    Test findById(int id);
}

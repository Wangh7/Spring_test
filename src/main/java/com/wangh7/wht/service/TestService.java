package com.wangh7.wht.service;


import com.wangh7.wht.dao.TestDAO;
import com.wangh7.wht.pojo.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TestService {
    @Autowired
    TestDAO testDAO;
    public void list() {
        Date date = new Date();
        date.setTime(testDAO.findById(1).getBig());
        System.out.println(date);
    }
}

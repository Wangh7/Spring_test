package com.wangh7.wht.service;


import com.wangh7.wht.dao.TestDAO;
import com.wangh7.wht.pojo.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {
    @Autowired
    TestDAO testDAO;
    public List<Test> list() { //获取所有图书
        Sort sort = Sort.by(Sort.Direction.DESC, "us123er_id");
        return testDAO.findAll(sort);

    }
}

package com.wangh7.wht.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemCheck {
    int itemId;
    int managerId;
    String newPassword;
    long checkTime;

    BigDecimal price;
}

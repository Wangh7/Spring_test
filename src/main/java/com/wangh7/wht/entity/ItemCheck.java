package com.wangh7.wht.entity;

import lombok.Data;
import org.hibernate.annotations.Type;
import org.joda.money.Money;

import java.math.BigDecimal;

@Data
public class ItemCheck {
    int itemId;
    int managerId;
    String newPassword;
    String checkTime;

    BigDecimal price;
}

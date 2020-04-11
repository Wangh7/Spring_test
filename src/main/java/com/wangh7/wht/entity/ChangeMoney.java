package com.wangh7.wht.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ChangeMoney {
    String status;
    BigDecimal money;
}

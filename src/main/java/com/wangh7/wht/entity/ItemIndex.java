package com.wangh7.wht.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemIndex {
    int totalItem;
    double totalPrice;
    int totalSellN;
    int totalSellW;//实体卡等待发货
    int totalSellF;//电子卡审核失败
    int totalSellF1;//实体卡审核失败，退款
    int totalBuyCar;
    int totalBought;
}

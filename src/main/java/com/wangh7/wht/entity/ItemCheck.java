package com.wangh7.wht.entity;

import lombok.Data;

@Data
public class ItemCheck {
    int itemId;
    int managerId;
    String newPassword;
    String checkTime;
}

package com.wangh7.wht.pojo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "item_sell")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"}) //标注哪个属性不用转json
public class ItemSell {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int itemId;

    int userId;
    int managerId;
    String cardNum;
    String cardPass;
    BigDecimal price;
    String createTime;
    String dueTime;
    String status;
    String checkTime;

    @JoinColumn(name = "type_id")
    @ManyToOne
    private ItemType itemType;

}

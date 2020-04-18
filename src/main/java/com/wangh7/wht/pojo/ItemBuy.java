package com.wangh7.wht.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "item_buy")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"}) //标注哪个属性不用转json
public class ItemBuy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    int userId;

    @JoinColumn(name = "itemId")
    @OneToOne
    ItemStock itemStock;

    String status;
    BigDecimal price;
    long createTime;
    long finishTime;
}

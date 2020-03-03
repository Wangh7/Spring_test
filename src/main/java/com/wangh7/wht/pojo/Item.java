package com.wangh7.wht.pojo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "item")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"}) //标注哪个属性不用转json

public class Item {
    @Getter
    @Setter
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int itemId;
    @Getter
    @Setter
    @Column(name = "user_id")
    int userId;
    @Getter
    @Setter
    @Column(name = "old_price")
    int oldPrice;
    @Getter
    @Setter
    @Column(name = "new_price")
    int newPrice;
    @Getter
    @Setter
    @Column(name = "name")
    String itemName;
}

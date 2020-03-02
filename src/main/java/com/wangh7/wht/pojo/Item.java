package com.wangh7.wht.pojo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="item")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"}) //标注哪个属性不用转json

public class Item {
    @Getter
    @Setter
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int item_id;
    @Getter
    @Setter
    @Column(name = "user_id")
    int user_id;
    @Getter
    @Setter
    @Column(name = "old_price")
    int old_price;
    @Getter
    @Setter
    @Column(name = "new-price")
    int new_price;
    @Getter
    @Setter
    @Column(name = "name")
    String item_name;
}

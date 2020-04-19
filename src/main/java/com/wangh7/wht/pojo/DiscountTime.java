package com.wangh7.wht.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "discount_time")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"}) //标注哪个属性不用转json
public class DiscountTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String timeName;
    int timeLeftday;
    double discountSell;
    double discountBuy;
    boolean enabled;
}

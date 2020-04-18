package com.wangh7.wht.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "item_timeline")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"}) //标注哪个属性不用转json
public class ItemTimeline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    int itemId;
    long timestamp;
    String content;
    String type;
    String icon;
    String status;

}

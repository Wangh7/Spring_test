package com.wangh7.wht.pojo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "item_type")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"}) //标注哪个属性不用转json
public class ItemType {
    @Getter
    @Setter
    @Id
    @Column(name = "type_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int typeId;
    @Getter
    @Setter
    @Column(name = "type_name")
    String typeName;
    @Getter
    @Setter
    @Column(name = "type_code")
    String typeCode;
}

package com.wangh7.wht.pojo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.joda.money.Money;

import javax.persistence.*;

@Data
@Entity
@Table(name = "item_stock")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"}) //标注哪个属性不用转json
public class ItemStock {
    @Id
    int itemId;

    int managerId;
    String cardNum;
    String cardPass;
    String createTime;
    String dueTime;
    String status;
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyAmount",
            parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "CNY")})
    Money price;

    @JoinColumn(name = "type_id")
    @ManyToOne
    private ItemType itemType;
}

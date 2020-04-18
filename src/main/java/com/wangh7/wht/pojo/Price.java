package com.wangh7.wht.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.joda.money.Money;

import javax.persistence.*;

@Entity
@Data
@Table(name = "money")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    int userId;

    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyAmount",
    parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "CNY")})
    Money money;

}

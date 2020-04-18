package com.wangh7.wht.pojo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "test")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"}) //标注哪个属性不用转json

public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    //自增主键

    int id;

    Timestamp timestamp;

    long big;

}

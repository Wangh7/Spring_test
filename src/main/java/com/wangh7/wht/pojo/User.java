package com.wangh7.wht.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity //实体类
@Table(name = "user")   //对应表名
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"}) //标注哪个属性不用转json
public class User {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    //自增主键
    @Column(name = "id")
    int id;
    @Getter
    @Setter
    @Column(name = "username")
    String username;
    @Getter
    @Setter
    @Column(name = "password")
    String password;
}

package com.wangh7.wht.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.persistence.*;

@Data
@Entity //实体类
@Table(name = "user")   //对应表名
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"}) //标注哪个属性不用转json
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    //自增主键
    @Column(name = "id")
    int id;

    @Column(name = "username")
    String username;

    @Column(name = "password")
    String password;

    @Column(name = "usertype")
    String usertype;

    @Column(name = "salt")
    String salt;

    @Column(name = "phone")
    String phone;
}

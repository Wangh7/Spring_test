package com.wangh7.wht.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.util.List;

@Data
@Entity //实体类
@Table(name = "user")   //对应表名
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"}) //标注哪个属性不用转json
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    //自增主键
    @Column(name = "id")
    int id;

    String username;

    String password;

    String nickname;

    String salt;

    String phone;

    boolean enabled;

    @Transient
    List<Role> roles;

    public User(){}

    public User(int id, String username, String nickname, String phone, boolean enabled) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.phone = phone;
        this.enabled = enabled;
    }
    public User(int id, String username, String nickname, String phone) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.phone = phone;
    }
}

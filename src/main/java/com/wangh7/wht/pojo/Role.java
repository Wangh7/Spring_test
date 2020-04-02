package com.wangh7.wht.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "role")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    //自增主键
    @Column(name = "id")
    int id;
    String name;
    String nameZh;
}

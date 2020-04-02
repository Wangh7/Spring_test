package com.wangh7.wht.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "role_menu")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class RoleMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    //自增主键
    @Column(name = "id")
    int id;
    int roleId;
    int menuId;
}

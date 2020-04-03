package com.wangh7.wht.pojo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "permission")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    String name;
    String desc;
    String url;
}

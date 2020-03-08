package com.wangh7.wht.pojo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@Table(name = "item")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"}) //标注哪个属性不用转json

public class Item {
    @Getter
    @Setter
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int itemId;
    @Getter
    @Setter
    @Column(name = "user_id")
    int userId;
    @Getter
    @Setter
    @Column(name = "image")
    String image;
    @Getter
    @Setter
    @Column(name = "old_price")
    float oldPrice;
    @Getter
    @Setter
    @Column(name = "new_price")
    float newPrice;
    @Getter
    @Setter
    @Column(name = "name")
    String itemName;
    @Getter
    @Setter
    @Column(name = "create_date")
    String createDate;
    @Getter
    @Setter
    @Column(name = "due_date")
    String dueDate;
    @Getter
    @Setter
    @JoinColumn(name = "type_id")
    @ManyToOne
    //商品与类别是多对一的关系，JoinColumn用来加入外键，Cacade用来级联，
    // CascadeType.Remove 指级联删除，被关联表元祖删除，关联表的对应元祖也会被删除，这里item是关联表，
    // 通过外键字段itemType(在数据库中为type_id外键）关联type表，type是被关联表
    // 同样 CascadeType.Persist则是级联存储，其他几个 对应他们的本身单词意思
    //CascadType.ALL 则包含了所有级联操作
    private ItemType itemType;
}

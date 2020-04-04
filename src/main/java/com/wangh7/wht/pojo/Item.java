package com.wangh7.wht.pojo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


import javax.persistence.*;

@Data
@Entity
@Table(name = "item")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"}) //标注哪个属性不用转json

public class Item {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int itemId;

    int userId;

    String image;

    float oldPrice;

    float newPrice;

    @Column(name = "name")
    String itemName;

    String createDate;

    String dueDate;

    @JoinColumn(name = "type_id")
    @ManyToOne
    //商品与类别是多对一的关系，JoinColumn用来加入外键，Cacade用来级联，
    // CascadeType.Remove 指级联删除，被关联表元祖删除，关联表的对应元祖也会被删除，这里item是关联表，
    // 通过外键字段itemType(在数据库中为type_id外键）关联type表，type是被关联表
    // 同样 CascadeType.Persist则是级联存储，其他几个 对应他们的本身单词意思
    //CascadType.ALL 则包含了所有级联操作
    private ItemType itemType;

    String status;
}

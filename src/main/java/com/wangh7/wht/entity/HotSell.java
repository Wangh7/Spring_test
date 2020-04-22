package com.wangh7.wht.entity;

import lombok.Data;

@Data
public class HotSell {
    int typeId;
    String typeName;
    String typeCode;
    long sellCount;

    public HotSell(int typeId,String typeName, String typeCode,long sellCount){
        super();
        this.typeId = typeId;
        this.sellCount = sellCount;
        this.typeName = typeName;
        this.typeCode = typeCode;
    }
}

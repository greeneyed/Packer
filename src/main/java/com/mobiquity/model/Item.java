package com.mobiquity.model;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class Item {

    Integer index;
    BigDecimal weight;
    BigDecimal cost;
    boolean isChosen;

    public Item(Integer index, BigDecimal weight, BigDecimal cost) {
        this.index = index;
        this.weight = weight;
        this.cost = cost;
    }
}

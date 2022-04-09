package com.mobiquity.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    Integer index;
    BigDecimal weight;
    BigDecimal cost;

}

package com.mobiquity.model;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Test case parsed from input file line
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InputLine {

    BigDecimal targetWeight;

    List<Item> items;

}

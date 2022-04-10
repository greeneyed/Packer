package com.mobiquity.model;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Single test case parsed from input file line
 *
 */
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class InputLine {

    BigDecimal targetWeight;
    List<Item> items;

}

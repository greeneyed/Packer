package com.mobiquity.validator;

import com.mobiquity.exception.ParsingException;
import com.mobiquity.model.Item;
import java.math.BigDecimal;
import java.util.Objects;

public class ItemValidator implements Validator<Item> {

    protected static final BigDecimal MAX_VALUE = new BigDecimal(100);

    protected static final String ITEM_PATTERN = "\\(\\d*,\\d*(\\.\\d*)?,€\\d*(\\.\\d*)?\\)";

    @Override
    public void validate(Item item) {
        Objects.requireNonNull(item);
        if (MAX_VALUE.compareTo(item.getWeight()) < 0) {
            throw new ParsingException(
                String.format("Weight %.2f is out of bounds", item.getWeight()));
        }
        if (MAX_VALUE.compareTo(item.getCost()) < 0) {
            throw new ParsingException(
                String.format("Cost %.2f is out of bounds", item.getCost()));
        }
    }

    @Override
    public void validateInputString(String input) {
        Objects.requireNonNull(input);
        if (!input.matches(ITEM_PATTERN)) {
            throw new ParsingException(
                String.format("Item %s doesn't match required format", input));
        }
    }
}

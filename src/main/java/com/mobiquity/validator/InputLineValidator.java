package com.mobiquity.validator;

import com.mobiquity.exception.ParsingException;
import com.mobiquity.model.InputLine;
import java.math.BigDecimal;
import java.util.Objects;

public class InputLineValidator implements Validator<InputLine> {

    protected static final BigDecimal MAX_PACKAGE_WEIGHT = new BigDecimal(100);
    protected static final int MAX_ITEMS_NUMBER = 15;

    protected static final String INPUT_PATTERN = "\\d+\\s?:\\s?\\((.*)\\)+";

    @Override
    public void validate(InputLine inputLine) {
        Objects.requireNonNull(inputLine);
        if (MAX_PACKAGE_WEIGHT.compareTo(inputLine.getTargetWeight()) < 0) {
            throw new ParsingException(
                String.format("Target weight %.2f is out of bounds", inputLine.getTargetWeight()));
        }
        if (MAX_ITEMS_NUMBER < inputLine.getItems().size()) {
            throw new ParsingException(
                String.format("Items number %d is out of bounds", inputLine.getItems().size()));
        }
    }

    @Override
    public void validateInputString(String input) {
        Objects.requireNonNull(input);
        if (!input.matches(INPUT_PATTERN)) {
            throw new ParsingException(
                String.format("Input line %s doesn't match required format", input));
        }
    }
}

package com.mobiquity.validator;

import static org.junit.jupiter.api.Assertions.*;

import com.mobiquity.exception.ParsingException;
import com.mobiquity.model.InputLine;
import com.mobiquity.model.Item;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class InputLineValidatorTest {

    private final Validator<InputLine> validator = new InputLineValidator();

    @Test
    void shouldValidateInput() {
        InputLine inputLine = new InputLine(new BigDecimal(100),
            Collections.singletonList(new Item(1, BigDecimal.TEN, BigDecimal.TEN)));
        assertDoesNotThrow(() -> validator.validate(inputLine));
    }

    @Test
    void shouldThrowExceptionIfTargetWeightExceedsMax() {
        InputLine inputLine = new InputLine(new BigDecimal("100.01"),
            Collections.singletonList(new Item(1, BigDecimal.TEN, BigDecimal.TEN)));
        ParsingException exception = assertThrows(ParsingException.class,
            () -> validator.validate(inputLine));
        assertEquals("Target weight 100.01 is out of bounds", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfItemNumberExceedsMax() {
        List<Item> items = IntStream.range(0,16)
            .mapToObj(item -> new Item(1, BigDecimal.TEN, BigDecimal.TEN))
            .collect(Collectors.toList());
        InputLine inputLine = new InputLine(new BigDecimal("99.99"),
            items);
        ParsingException exception = assertThrows(ParsingException.class,
            () -> validator.validate(inputLine));
        assertEquals("Items number 16 is out of bounds", exception.getMessage());
    }

    @Test
    void shouldValidateValidInputString() {
        String input = "99 : () ()";
        assertDoesNotThrow(() -> validator.validateInputString(input));
    }

    @Test
    void shouldThrowExceptionIfInputContainsNegativeValues() {
        String input = "-1 : (11,22,33)";
        ParsingException exception = assertThrows(ParsingException.class,
            () -> validator.validateInputString(input));
        assertEquals("Input line -1 : (11,22,33) doesn't match required format", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfInputNotContainsItems() {
        String input = "10 : ";
        ParsingException exception = assertThrows(ParsingException.class,
            () -> validator.validateInputString(input));
        assertEquals("Input line 10 :  doesn't match required format", exception.getMessage());
    }

}
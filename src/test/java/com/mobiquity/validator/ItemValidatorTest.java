package com.mobiquity.validator;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mobiquity.exception.ParsingException;
import com.mobiquity.model.Item;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ItemValidatorTest {

    private final Validator<Item> validator = new ItemValidator();

    @Test
    void shouldValidateValidItem() {
        Item item = new Item(100, new BigDecimal(100), new BigDecimal(100));
        assertDoesNotThrow(() -> validator.validate(item));
    }

    @Test
    void shouldThrowExceptionIfWeightExceedsMax() {
        Item item = new Item(100, new BigDecimal("100.01"), new BigDecimal(1));
        ParsingException exception = assertThrows(ParsingException.class,
            () -> validator.validate(item));
        assertEquals("Weight 100.01 is out of bounds", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfCostExceedsMax() {
        Item item = new Item(100, new BigDecimal(3), new BigDecimal(110));
        ParsingException exception = assertThrows(ParsingException.class,
            () -> validator.validate(item));
        assertEquals("Cost 110.00 is out of bounds", exception.getMessage());
    }

    @Test
    void shouldValidateValidInputString() {
        String input = "(123,90,€12)";
        assertDoesNotThrow(() -> validator.validateInputString(input));
    }

    @Test
    void shouldThrowExceptionIfInputContainsNegativeValues() {
        String input = "(1,-90,€12)";
        ParsingException exception = assertThrows(ParsingException.class,
            () -> validator.validateInputString(input));
        assertEquals("Item (1,-90,€12) doesn't match required format", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfInputContainsTwoValues() {
        String input = "(1,90)";
        ParsingException exception = assertThrows(ParsingException.class,
            () -> validator.validateInputString(input));
        assertEquals("Item (1,90) doesn't match required format", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfInputContainsFourValues() {
        String input = "(1,9.55,€12,7)";
        ParsingException exception = assertThrows(ParsingException.class,
            () -> validator.validateInputString(input));
        assertEquals("Item (1,9.55,€12,7) doesn't match required format", exception.getMessage());
    }

}
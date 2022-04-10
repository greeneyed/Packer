package com.mobiquity.validator;

import com.mobiquity.exception.ParsingException;

public interface Validator<T> {

    /**
     * Checks if parsed object meets required constraints
     * @param object - object to check
     * @throws ParsingException if constraints are not met
     */
    void validate(T object);

    /**
     * Checks if parsed input meets required format pattern
     * @param input - input string to check
     * @throws ParsingException if input cannot be parsed
     */
    void validateInputString(String input);

}

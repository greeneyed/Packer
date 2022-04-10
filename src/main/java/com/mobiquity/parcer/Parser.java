package com.mobiquity.parcer;

import com.mobiquity.exception.APIException;
import com.mobiquity.exception.ParsingException;
import com.mobiquity.model.InputLine;
import com.mobiquity.model.Item;
import com.mobiquity.validator.InputLineValidator;
import com.mobiquity.validator.ItemValidator;
import com.mobiquity.validator.Validator;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

public class Parser {

    public static final char TOTAL_WEIGHT_SEPARATOR_CHAR = ':';
    public static final char PACKAGE_SEPARATOR_CHAR = ',';
    public static final String EUR_SIGN = "€";
    public static final String PARENTHESES = "()";

    private final Validator<InputLine> inputLineValidator = new InputLineValidator();
    private final Validator<Item> itemValidator = new ItemValidator();

    /**
     * Parse input file into list of test cases
     * @param inputFile absolute path to file to be parsed
     * @return list of model representation of input test cases
     * @throws APIException if input file is not exist or cannot be parsed
     */
    public List<InputLine> parse(String inputFile) throws APIException {
        if (StringUtils.isBlank(inputFile)) {
            return Collections.emptyList();
        }
        Path path = Path.of(inputFile);
        try (Stream<String> lines = Files.lines(path)){
            return lines.map(this::parseInputLine)
                .collect(Collectors.toList());
        } catch (ParsingException e) {
            throw new APIException("Failed to parse input file", e);
        } catch (NoSuchFileException e) {
            throw new APIException(String.format("404 File not found: %s", inputFile), e);
        } catch (IOException e) {
            throw new APIException(
                String.format("Exception occurred while parsing input file: %s", inputFile), e);
        }
    }

    private InputLine parseInputLine(String line) {
        inputLineValidator.validateInputString(line);

        String[] parts = StringUtils.split(line, TOTAL_WEIGHT_SEPARATOR_CHAR);
        BigDecimal targetWeight = parseDecimalValue(parts[0]);

        String[] itemList = StringUtils.split(parts[1].trim(), StringUtils.SPACE);
        List<Item> items = Arrays.stream(itemList)
            .map(this::parseItem)
            .collect(Collectors.toList());

        InputLine inputLine = new InputLine(targetWeight, items);
        inputLineValidator.validate(inputLine);
        return inputLine;
    }

    private Item parseItem(String stringItem) {
        itemValidator.validateInputString(stringItem);
        String[] packageItems = StringUtils
            .split(StringUtils.strip(stringItem, PARENTHESES), PACKAGE_SEPARATOR_CHAR);
        Item item = new Item(parseIntegerValue(packageItems[0]),
            parseDecimalValue(packageItems[1]),
            parseDecimalValue(StringUtils.substringAfter(packageItems[2], EUR_SIGN)));
        itemValidator.validate(item);
        return item;
    }

    private BigDecimal parseDecimalValue(String value) {
        return new BigDecimal(value.trim());
    }

    private int parseIntegerValue(String value) {
        return Integer.parseInt(value.trim());
    }

}

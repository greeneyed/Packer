package com.mobiquity.parcer;

import com.mobiquity.exception.APIException;
import com.mobiquity.model.InputLine;
import com.mobiquity.model.Item;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Parser {

    private static final Logger log = LogManager.getLogger(Parser.class);

    public static final char TOTAL_WEIGHT_SEPARATOR_CHAR = ':';
    public static final char PACKAGE_SEPARATOR_CHAR = ',';

    /**
     * Parse input file into list of test cases
     * @param inputFile absolute path to file to be parsed
     * @return list of model representation of input test cases
     * @throws APIException if input file is not exist or corrupted
     */
    public List<InputLine> parse(String inputFile) throws APIException {
        if (StringUtils.isBlank(inputFile)) {
            return Collections.emptyList();
        }
        Path path = Path.of(inputFile);
        try (Stream<String> lines = Files.lines(path)){

            return lines.map(this::parseInputLine)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        } catch (NoSuchFileException e) {
            throw new APIException(String.format("404 File not found: %s", inputFile), e);
        } catch (IOException e) {
            throw new APIException(
                String.format("Exception occurred while parsing input file: %s", inputFile), e);
        }
    }

    private InputLine parseInputLine(String line) {
        if (StringUtils.isBlank(line)) {
            log.warn("Empty input line");
            return null;
        }
        InputLine inputLine = new InputLine();
        String[] parts = StringUtils.split(line, TOTAL_WEIGHT_SEPARATOR_CHAR);
        inputLine.setTargetWeight(parseDecimalValue(parts[0]));

        String[] itemList = StringUtils.split(parts[1].trim(), StringUtils.SPACE);
        List<Item> items = Arrays.stream(itemList).map(stringItem -> {
            String[] packageItems = StringUtils
                .split(StringUtils.strip(stringItem, "()"), PACKAGE_SEPARATOR_CHAR);
            return new Item(parseIntegerValue(packageItems[0].trim()),
                parseDecimalValue(packageItems[1]),
                parseDecimalValue(StringUtils.substringAfter(packageItems[2], "€")));
        }).collect(Collectors.toList());
        inputLine.setItems(items);
        log.info("Input line: {}", inputLine);
        return inputLine;
    }

    private BigDecimal parseDecimalValue(String value) {
        return new BigDecimal(value.trim());
    }

    private int parseIntegerValue(String value) {
        return Integer.parseInt(value.trim());
    }

}

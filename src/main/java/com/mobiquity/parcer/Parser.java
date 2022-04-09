package com.mobiquity.parcer;

import com.mobiquity.exception.APIException;
import com.mobiquity.model.InputLine;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Parser {

    private static final Logger log = LogManager.getLogger(Parser.class);

    public List<InputLine> parse(String input) throws APIException {
        if (StringUtils.isBlank(input)) {
            return Collections.emptyList();
        }
        Path path = Path.of(input);
        try (Stream<String> lines = Files.lines(path)){
            return lines.map(line -> {
                    InputLine inputLine = new InputLine();
                    log.info("Line: {}", line);

                    return inputLine;
                }).collect(Collectors.toList());
        } catch (NoSuchFileException e) {
            throw new APIException(
                String.format("404 File not found: %s", input), e);
        } catch (IOException e) {
            throw new APIException(
                String.format("Exception occurred while parsing input file: %s", input), e);
        }
    }

}

package com.mobiquity.parcer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mobiquity.exception.APIException;
import com.mobiquity.model.InputLine;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;

class ParserTest {

    public static final URL RESOURCE = ParserTest.class.getResource("/");
    public static final String INPUT = "example_input";

    @Test
    void shouldParseInputFile() throws URISyntaxException, APIException {
        Parser parser = new Parser();
        List<InputLine> parsedLines = parser.parse(
            Objects.requireNonNull(RESOURCE).toURI().getPath() + INPUT);
        assertNotNull(parsedLines);
        assertEquals(4, parsedLines.size());
    }

    @Test
    void shouldThrowExceptionIfFileNotFound() {
        Parser parser = new Parser();
        APIException exception = assertThrows(APIException.class,
            () -> parser.parse("non-existing-file"));
        assertEquals(NoSuchFileException.class, exception.getCause().getClass());
    }

}
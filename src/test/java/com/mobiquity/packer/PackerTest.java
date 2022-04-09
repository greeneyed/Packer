package com.mobiquity.packer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mobiquity.exception.APIException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import org.junit.jupiter.api.Test;

class PackerTest {

    public static final URL RESOURCE = PackerTest.class.getResource("/");
    public static final String INPUT = "example_input";
    public static final String OUTPUT = "example_output";

    @Test
    void shouldPackInputFile() throws URISyntaxException, APIException, IOException {
        String result = Packer.pack(Objects.requireNonNull(RESOURCE).toURI().getPath() + INPUT);

        Path path = Paths.get(Objects.requireNonNull(RESOURCE).toURI().getPath() + OUTPUT);
        String expectedResult = String.join("\n", Files.readAllLines(path));
        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

}
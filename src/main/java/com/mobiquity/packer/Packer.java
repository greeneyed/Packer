package com.mobiquity.packer;

import com.mobiquity.exception.APIException;
import com.mobiquity.model.InputLine;
import com.mobiquity.parcer.Parser;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class Packer {

    private Packer() {
    }

    public static String pack(String filePath) throws APIException {
        Parser parser = new Parser();
        List<InputLine> inputLines = parser.parse(filePath);

        return inputLines.stream()
            .map(Packer::calculatePackage)
            .collect(Collectors.joining(StringUtils.LF));
    }

    protected static String calculatePackage(InputLine inputLine) {
        return "not implemented";
    }


    public static void main(String[] args) throws APIException {
        Packer.pack(
            "/Users/Maryna_Ukrainska/IdeaProjects/skeleton_java/src/test/resources/example_input");
    }

}

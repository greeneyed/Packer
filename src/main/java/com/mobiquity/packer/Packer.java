package com.mobiquity.packer;

import com.mobiquity.exception.APIException;
import com.mobiquity.model.InputLine;
import com.mobiquity.parcer.Parser;
import java.util.List;

public class Packer {

  private Packer() {
  }

  public static String pack(String filePath) throws APIException {
    Parser parser = new Parser();
    List<InputLine> inputLines = parser.parse(filePath);
    return null;
  }

}

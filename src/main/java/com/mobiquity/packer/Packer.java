package com.mobiquity.packer;

import com.mobiquity.exception.APIException;
import com.mobiquity.model.InputLine;
import com.mobiquity.model.Item;
import com.mobiquity.parcer.Parser;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Packer {

    public static final Logger log = LogManager.getLogger(Packer.class);

    private Packer() {
    }

    public static String pack(String filePath) throws APIException {
        Parser parser = new Parser();
        List<InputLine> inputLines = parser.parse(filePath);

        return inputLines.stream()
            .map(inputLine -> {
                List<Item> pack = new ArrayList<>();

                BigDecimal totalCost = calculatePackage(inputLine.getTargetWeight(),
                    inputLine.getItems(), inputLine.getItems().size(), pack);
                log.info("For target weight: {} total cost is {}",
                    inputLine.getTargetWeight(), totalCost);

                if (pack.isEmpty()) {
                    return "-";
                }
                return pack.stream()
                    .map(item -> item.getIndex().toString())
                    .collect(Collectors.joining(","));
            })
            .collect(Collectors.joining(StringUtils.LF));
    }

    /**
     * Algorithm to find
     *
     * @param totalWeight total weight of package
     * @param items list of items to be send
     * @param itemsNumber number of items
     * @param pack package of items that fits weigh restrictions with maximum cost of chosen items
     * @return maximum cost of items in package
     */
    private static BigDecimal calculatePackage(BigDecimal totalWeight, List<Item> items,
        int itemsNumber, List<Item> pack) {
        if (BigDecimal.ZERO.equals(totalWeight) || itemsNumber == 0) {
            return BigDecimal.ZERO;
        }
        Item currentItem = items.get(itemsNumber - 1);

        if (currentItem.getWeight().compareTo(totalWeight) > 0) {
            return calculatePackage(totalWeight, items, itemsNumber - 1, pack);
        } else {
            int beforeIncludePackSize = pack.size();
            BigDecimal included = currentItem.getCost().add(
                calculatePackage(totalWeight.subtract(currentItem.getWeight()), items,
                    itemsNumber - 1, pack));

            int beforeExcludePackSize = pack.size();
            BigDecimal excluded = calculatePackage(totalWeight, items, itemsNumber - 1, pack);

            if (included.compareTo(excluded) >= 0) {
                if (pack.size() > beforeExcludePackSize) {
                    pack.subList(beforeExcludePackSize, pack.size()).clear();
                }
                pack.add(currentItem);
                return included;
            } else {
                if (beforeExcludePackSize > beforeIncludePackSize) {
                    pack.subList(beforeIncludePackSize, beforeExcludePackSize).clear();
                }
                return excluded;
            }
        }
    }

}

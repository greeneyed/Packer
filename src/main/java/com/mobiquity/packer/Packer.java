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

    /**
     * Provides list of items with biggest cost where sum of their weights is smaller than or equal to totalWeight
     *
     * @param filePath absolute path to input file
     * @return indexes of chosen items, each testcase result is a new row in the output string
     * @throws APIException if input does not meet restrictions or cannot be parsed
     */
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
     * Algorithm to find out the maximum cost subset of items
     * such that sum of the weights of these items is smaller than or equal to totalWeight.
     *
     * Knapsack problem could be solved in 2 different ways: recursively or with dynamic programming approach.
     * Recursive solution has been chosen because weights have values with decimal point so they cannot be looped.
     * Recursive solution complexity is O(2^n) with restriction on maximum 15 items it is max 32768 iterations.
     * Meanwhile, dynamic programming solution complexity is O(nW), but even if convert kilos to grams
     * with provided restrictions the complexity would be 15 items * 10000 (100 kg in grams) = 150000.
     * So despite the fact that dynamic programming is faster on bigger numbers of items, recursive solution
     * is more than 4 times faster for provided restrictions.
     *
     * @param totalWeight total weight of package
     * @param items list of items to be send
     * @param itemsNumber number of items
     * @param pack package of items that fits weigh restrictions with maximum cost of chosen items
     * @return maximum cost of items in package
     */
    private static BigDecimal calculatePackage(BigDecimal totalWeight, List<Item> items,
        int itemsNumber, List<Item> pack) {

        // base case, recursion exit point
        if (BigDecimal.ZERO.equals(totalWeight) || itemsNumber == 0) {
            return BigDecimal.ZERO;
        }

        Item currentItem = items.get(itemsNumber - 1);

        // if weight of the current N-th item is more than package total capacity,
        // then this item cannot be included in the optimal solution
        if (currentItem.getWeight().compareTo(totalWeight) > 0) {
            return calculatePackage(totalWeight, items, itemsNumber - 1, pack);
        } else {
            // save current pack size before include next item
            int beforeIncludePackSize = pack.size();

            // if item included increase total cost by its cost,
            // reduce total capacity by its weight and check next item
            BigDecimal included = currentItem.getCost().add(
                calculatePackage(totalWeight.subtract(currentItem.getWeight()), items,
                    itemsNumber - 1, pack));

            // save current pack size before not include next item
            int beforeExcludePackSize = pack.size();
            // if item is not included then check next item
            BigDecimal notIncluded = calculatePackage(totalWeight, items, itemsNumber - 1, pack);

            // return the maximum of two cases: N-th item included or N-th item is not included
            if (included.compareTo(notIncluded) >= 0) {
                // clear not optimal items in package
                if (pack.size() > beforeExcludePackSize) {
                    pack.subList(beforeExcludePackSize, pack.size()).clear();
                }
                // add optimal item to package
                pack.add(currentItem);
                return included;
            } else {
                // clear not optimal items in package
                if (beforeExcludePackSize > beforeIncludePackSize) {
                    pack.subList(beforeIncludePackSize, beforeExcludePackSize).clear();
                }
                return notIncluded;
            }
        }
    }

}

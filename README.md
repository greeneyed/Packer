## Packer

### Algorithm chose overview
Algorithm to find out the maximum cost subset of items such that sum of the weights of these items is smaller than or equal to totalWeight.

Knapsack problem could be solved in 2 different ways: recursively or with dynamic programming approach.

Recursive solution has been chosen because weights have values with decimal point, so they cannot be looped.

**Recursive solution complexity is O(2^n)** with restriction on maximum 15 items it is max **32768 iterations**.
Meanwhile, **dynamic programming solution complexity is O(nW)**, but even if convert kilos to grams with provided restrictions the complexity would be 15 items * 10000 (100 kg in grams) = **150000 iterations**.
So despite the fact that dynamic programming is faster on bigger numbers of items, recursive solution is more than 4 times faster for provided restrictions.

### Classes overview
Library contains 3 main instances:
* Packer - Reads input file and calculate which items should be put to package
* Parser - Parse input file into model representation of input test cases
* Validator - Validate the correctness of input and checks the provided constrains
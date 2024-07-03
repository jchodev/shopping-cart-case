## Notes(Need clarification on this requirement):
- For "A buy 2 get 1 free promotion" & "A combination purchase deal" ,  is it allow to apply multiple times to a same shopping cart? :

## Assumptions
- Added the `repeatable` flag of  `BuyGetFreePromotion` and `CombinationPurchaseDeal` class level
  - A flag indicating whether this promotion can be applied multiple times to the same shopping cart (default: true).
  - If set to `false`, the promotion can only be applied once, even if the required categories appear multiple times in the cart.
  - I believe that these changes will improve the flexibility of these discount promotions
  - By allowing them to be marked as non-repeatable (repeatable=false), can create more complex discount scenarios.
    - For example:
      - A "Buy 2, Get 1 Free" promotion can be set as non-repeatable to ensure it only applies once, even if a customer purchases multiples of 2 qualifying items.
      - A combination purchase deal with a discounted bundle price can be set as non-repeatable to prevent the discounted bundle from being applied multiple times if the required categories appear several times in the cart.

## Suggestions
1. Add the checking mechanism of `Buy X, Get Y Free` to make sure X is larger than Y
2. Add the `reason` or `remark` paramater on `PromotionApplicationResult` when applied promotion/discount failed, that is more easy to track
3. Add JaCoCo library (https://github.com/jacoco/jacoco) which helps developers to measure the quality and completeness of their tests by providing information on which parts of the codebase are covered by tests and which are not.
4. Add more test case to cover more different of case

## Covered test case
- Buy X, Get Free Promotion - [https://docs.google.com/spreadsheets/d/1bBjfQ2zHCByVQeS-EvMYhOvbkSkRGSoieG8a6BGFJ00/edit?gid=0#gid=0](https://docs.google.com/spreadsheets/d/1bBjfQ2zHCByVQeS-EvMYhOvbkSkRGSoieG8a6BGFJ00/edit?gid=0#gid=0)
- X% off total basket - [https://docs.google.com/spreadsheets/d/1bBjfQ2zHCByVQeS-EvMYhOvbkSkRGSoieG8a6BGFJ00/edit?gid=0#gid=0](https://docs.google.com/spreadsheets/d/1bBjfQ2zHCByVQeS-EvMYhOvbkSkRGSoieG8a6BGFJ00/edit?gid=61445916#gid=61445916)
- X% off all {Product} - [https://docs.google.com/spreadsheets/d/1bBjfQ2zHCByVQeS-EvMYhOvbkSkRGSoieG8a6BGFJ00/edit?gid=0#gid=0](https://docs.google.com/spreadsheets/d/1bBjfQ2zHCByVQeS-EvMYhOvbkSkRGSoieG8a6BGFJ00/edit?gid=723315935#gid=723315935)
- MealDeal X Food  + X Drink  + X Fruit - [https://docs.google.com/spreadsheets/d/1bBjfQ2zHCByVQeS-EvMYhOvbkSkRGSoieG8a6BGFJ00/edit?gid=0#gid=0](https://docs.google.com/spreadsheets/d/1bBjfQ2zHCByVQeS-EvMYhOvbkSkRGSoieG8a6BGFJ00/edit?gid=293844877#gid=293844877)
- Support different discounts/promotions  - [https://docs.google.com/spreadsheets/d/1bBjfQ2zHCByVQeS-EvMYhOvbkSkRGSoieG8a6BGFJ00/edit?gid=1719329075#gid=1719329075](https://docs.google.com/spreadsheets/d/1bBjfQ2zHCByVQeS-EvMYhOvbkSkRGSoieG8a6BGFJ00/edit?gid=1719329075#gid=1719329075)

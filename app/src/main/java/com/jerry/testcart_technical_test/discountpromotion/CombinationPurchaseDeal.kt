package com.jerry.testcart_technical_test.discountpromotion

import com.jerry.testcart_technical_test.models.*


//A combination purchase deal (eg buy 1 sandwich and 1 apple and 1 drink for £X price)
class CombinationPurchaseDeal(private val mealDealCategories: List<MealDealCategory>,
                              private val dealPrice: Double,
                              /**
                               * A flag indicating whether this promotion can be applied multiple times to the same shopping cart (default: true).
                               * If set to `false`, the promotion can only be applied once, even if the required categories appear multiple times in the cart.
                               */
                              private val repeatable: Boolean = true,
): DiscountPromotion() {

    /**
     * Applies the combination purchase deal promotion to the list of shopping cart items.
     */
    override suspend fun apply(items: List<ShoppingCartItem>): List<PromotionApplicationResult> {
        val results = mutableListOf<PromotionApplicationResult>()
        var currentItems = items

        do {
            val result = applyDealOnce(currentItems)
            if (result != null) {
                currentItems = result.remainedItems
                results.add(result)
            }
        } while (result != null && repeatable)

        return results
    }

    /**
     * Applies the combination purchase deal promotion once and returns the result.
     */
    private fun applyDealOnce(items: List<ShoppingCartItem>): PromotionApplicationResult? {
        if (items.isEmpty()) return null

        // Separate items that support the combination purchase deal
        val (dealItems, remainingItems) = items.partition { it.product.supportCombinationPurchaseDeal }

        if (dealItems.isEmpty()) return null

        // Group items by category and sort by price (ascending)
        val groupedItems = dealItems.groupBy { it.product.category }
            .mapValues { it.value.sortedBy { item -> item.product.price } }

        applyMealDeal(groupedItems)?.let { (appliedProducts, remainedProducts)->
            return createPromotionResult(items, appliedProducts, remainedProducts.map { (product, qty) ->
                ShoppingCartItem(
                    product = product,
                    qty = qty
                )
            } + remainingItems)
        }

        return null
    }

    /**
     * Applies the meal deal to the grouped items and returns the applied and remaining products.
     */
    private fun applyMealDeal(groupedItems: Map<Category, List<ShoppingCartItem>>): Pair<Map<Product, Int>, Map<Product, Int>>? {
        val appliedProducts = mutableMapOf<Product, Int>()
        val remainedProducts = mutableMapOf<Product, Int>()

        mealDealCategories.forEach { category ->
            //Check if there have items under this mealDeal Category or not
            val categoryItems = groupedItems[category.category] ?: return null
            var remainingNeeded = category.qty

            for (item in categoryItems) {
                if (remainingNeeded > 0) {
                    val neededQty = minOf(item.qty, remainingNeeded)
                    appliedProducts[item.product] = neededQty
                    // Check if there are remaining items after deducting the needed quantity
                    if (item.qty - neededQty > 0) {
                        // Add the remaining items to the remainedProducts map
                        remainedProducts[item.product] = item.qty - neededQty
                    }

                    remainingNeeded -= neededQty

                } else {
                    // If there are no more items needed in this category, add all remaining items to the remainedProducts map
                    remainedProducts[item.product] = item.qty
                }
            }

            //there have not enough item to fulfill this meal deal
            if (remainingNeeded > 0) return null
        }

        return Pair(appliedProducts, remainedProducts)
    }

    /**
     * Creates a promotion application result.
     */
    private fun createPromotionResult(
        originalItems: List<ShoppingCartItem>,
        appliedProducts: Map<Product, Int>,
        remainedItems: List<ShoppingCartItem>
    ): PromotionApplicationResult {
        val appliedItems = appliedProducts.map { (product, qty) ->
            ShoppingCartItem(product, qty)
        }
        val remainedItemsList = remainedItems.map { (product, qty) ->
            ShoppingCartItem(product, qty)
        }

        return PromotionApplicationResult(
            originalItems = originalItems,
            appliedItems = appliedItems,
            remainedItems = remainedItemsList,
            appliedPrice = dealPrice,
            discountPromotion = this
        )
    }
}
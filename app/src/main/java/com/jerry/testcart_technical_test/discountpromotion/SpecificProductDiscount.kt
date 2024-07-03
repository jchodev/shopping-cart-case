package com.jerry.testcart_technical_test.discountpromotion

import com.jerry.testcart_technical_test.models.*


class SpecificProductDiscount(private val product: Product, private val percentageOff: Double) : DiscountPromotion() {

    /**
     * Applies the specific product discount to the list of shopping cart items.
     */
    override fun apply(items: List<ShoppingCartItem>): List<PromotionApplicationResult> {
        return calculateDiscount(items)?.let { listOf(it) } ?: emptyList()
    }

    /**
     * Calculates the discount for the specific product in the given items and returns the result.
     */
    private fun calculateDiscount(items: List<ShoppingCartItem>): PromotionApplicationResult? {
        if (items.isEmpty()) return null

        /**
         * Partitions the items into those that match the specific product and those that do not.
         */
        val (matchedItems, remainingItems) = items.partition { it.product.id == product.id }

        if (matchedItems.isEmpty()) return null

        /**
         * Calculates the discount amount for the matched items.
         */
        val discountAmount =  matchedItems.first().product.price * matchedItems.first().qty * (100 - percentageOff) / 100

        return PromotionApplicationResult(
            originalItems = items,
            appliedItems = matchedItems,
            remainedItems = remainingItems,
            appliedPrice = discountAmount,
            discountPromotion = this
        )
    }
}
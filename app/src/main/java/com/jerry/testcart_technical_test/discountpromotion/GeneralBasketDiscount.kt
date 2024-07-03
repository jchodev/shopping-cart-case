package com.jerry.testcart_technical_test.discountpromotion

import com.jerry.testcart_technical_test.models.*


//A general basket % discount (eg 10% off total basket)
class GeneralBasketDiscount(private val percentageOff: Double) : DiscountPromotion() {

    /**
     * Applies the general basket discount to the list of shopping cart items.
     */
    override fun apply(items: List<ShoppingCartItem>): List<PromotionApplicationResult> {
        val result = calculateDiscount(items)
        return result?.let { listOf(it) } ?: emptyList()
    }

    /**
     * Calculates the discount for the given items and returns the result.
     */
    private fun calculateDiscount(items: List<ShoppingCartItem>): PromotionApplicationResult? {
        if (items.isEmpty()) return null

        val totalAmount = items.sumOf { it.product.price * it.qty }
        val discountedAmount = totalAmount * (100 - percentageOff) / 100

        return PromotionApplicationResult(
            originalItems = items,
            appliedItems = items,
            remainedItems = emptyList(),
            appliedPrice = discountedAmount,
            discountPromotion = this
        )
    }
}

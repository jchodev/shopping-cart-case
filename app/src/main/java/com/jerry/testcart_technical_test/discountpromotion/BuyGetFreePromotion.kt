package com.jerry.testcart_technical_test.discountpromotion

import com.jerry.testcart_technical_test.models.*


//A buy 2 get 1 free promotion (eg buy 2 apples, get 3rd apple free)
class BuyGetFreePromotion(
    private val product: Product,
    private val buy: Int,
    private val free: Int,
    private val repeatable: Boolean = true,
) : DiscountPromotion()  {

    /**
     * Applies the buy-get-free promotion to the list of shopping cart items.
     */
    override fun apply(items: List<ShoppingCartItem>): List<PromotionApplicationResult> {
        val results = mutableListOf<PromotionApplicationResult>()
        var currentItems = items

        do {
            val result = applyPromotionOnce(currentItems)
            if (result != null) {
                currentItems = result.remainedItems
                results.add(result)
            }
        } while (result != null && repeatable)

        return results
    }

    /**
     * Applies the buy-get-free promotion once and returns the result.
     */
    private fun applyPromotionOnce(items: List<ShoppingCartItem>): PromotionApplicationResult? {
        if (items.isEmpty()) return null

        val (matchedItems, remainingItems) = items.partition { it.product.id == product.id && it.qty >= (buy + free) }

        if (matchedItems.isEmpty()) return null

        val totalQty = matchedItems.sumOf { it.qty }
        if (totalQty < (buy + free)) return null

        val appliedQty = buy + free
        val remainingQty = totalQty - appliedQty
        val discountAmount = product.price * buy

        val appliedItems = listOf(ShoppingCartItem(product, appliedQty))
        val updatedRemainingItems = if (remainingQty > 0) {
            remainingItems + ShoppingCartItem(product, remainingQty)
        } else {
            remainingItems
        }

        return PromotionApplicationResult(
            originalItems = items,
            appliedItems = appliedItems,
            remainedItems = updatedRemainingItems,
            appliedPrice = discountAmount,
            discountPromotion = this
        )
    }

}
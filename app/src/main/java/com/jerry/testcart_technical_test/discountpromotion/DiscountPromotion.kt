package com.jerry.testcart_technical_test.discountpromotion

import com.jerry.testcart_technical_test.models.PromotionApplicationResult
import com.jerry.testcart_technical_test.models.ShoppingCartItem


abstract class DiscountPromotion {
    abstract suspend fun apply(items : List<ShoppingCartItem>): List<PromotionApplicationResult>
}


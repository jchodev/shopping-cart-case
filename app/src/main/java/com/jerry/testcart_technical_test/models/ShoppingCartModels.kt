package com.jerry.testcart_technical_test.models

import com.jerry.testcart_technical_test.discountpromotion.DiscountPromotion


data class Category(
    val id: String,
    val name: String
)

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val category: Category,
    /**
     * Indicates whether this product can be part of a combination purchase deal or meal deal promotion.
     * For instance, Coke and Beer are both categorized as drinks, but this flag differentiates whether they can be included in a combination purchase deal or meal deal promotion.
     */
    val supportCombinationPurchaseDeal: Boolean = false,
)

data class ShoppingCartItem(
    val product: Product,
    val qty: Int
)

data class ShoppingCart(
    val items: List<ShoppingCartItem> = emptyList(),
    var appliedPromotionDiscounts: List<PromotionApplicationResult> = emptyList(),
    val totalAmount: Double = 0.0,
)

data class MealDealCategory(
    val category: Category,
    val qty: Int
)

/**
 * PromotionApplicationResult: Represents the result of applying a discount/promotion to a shopping cart.
 *
 * originalItems: The original items in the shopping cart before applying the discount/promotion
 * appliedItems: The items from the original list that were eligible for and received the discount/promotion
 * remainedItems: The items from the original list that did not meet the discount/promotion criteria or have remaining quantity after applying the  discount/promotion
 * appliedPrice: The total discount/price applied as a result of this discount/promotion
 * discountPromotion: The specific discount/promotion that was applied to this result
 */
data class PromotionApplicationResult(
    val originalItems: List<ShoppingCartItem> = emptyList(),
    val appliedItems: List<ShoppingCartItem> = emptyList(),
    val remainedItems: List<ShoppingCartItem> = emptyList(),
    val appliedPrice: Double = 0.0,
    val discountPromotion : DiscountPromotion,
)
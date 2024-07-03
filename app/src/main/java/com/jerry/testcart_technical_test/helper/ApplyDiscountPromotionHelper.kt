package com.jerry.testcart_technical_test.helper

import com.jerry.testcart_technical_test.discountpromotion.DiscountPromotion
import com.jerry.testcart_technical_test.models.ShoppingCart
import com.jerry.testcart_technical_test.models.ShoppingCartItem
import com.jerry.testcart_technical_test.utils.permutations


/**
 * This class helps apply discount promotions to a shopping cart and returns the cart with the minimum total amount.
 * It considers all possible permutations of discount applications to find the most optimal discount combination.
 */
class ApplyDiscountPromotionHelper {

    /**
     * Applies discount promotions to the shopping cart and returns the cart with the minimum total amount.
     */
    suspend fun applyDiscountPromotion(
        shoppingCart: ShoppingCart,
        discountPromotions: List<DiscountPromotion>
    ): ShoppingCart {

        // Generate all permutations of discount promotions
        val allPermutations  = discountPromotions.permutations()

        // Calculate the resulting shopping carts for each permutation
        val resultCarts = allPermutations.map { permutation ->
            calculateShoppingCart(shoppingCart, permutation)
        }

        // Return the shopping cart with the minimum total amount
        return resultCarts.minByOrNull { it.totalAmount } ?: shoppingCart
    }


    /**
     * Calculates the shopping cart after applying the given list of discount promotions.
     */
    private suspend fun calculateShoppingCart(
        shoppingCart: ShoppingCart,
        discountPromotions: List<DiscountPromotion>
    ): ShoppingCart {
        var updatedCart = shoppingCart

        // Apply each discount promotion sequentially
        discountPromotions.forEach { promotion ->
            val appliedDiscounts = promotion.apply(
                items = getRemainingItems(updatedCart)
            )

            // If discounts were applied, update the shopping cart
            if (appliedDiscounts.isNotEmpty()) {
                updatedCart = updatedCart.copy(
                    appliedPromotionDiscounts = updatedCart.appliedPromotionDiscounts + appliedDiscounts,
                )
            }
        }

        // Calculate the total applied discount amount
        val totalDiscountAmount = updatedCart.appliedPromotionDiscounts.sumOf { it.appliedPrice }

        // Determine the remaining items after applying discounts
        val remainingItems = getRemainingItems(updatedCart)

        // Calculate the total amount for the remaining items
        val remainingItemsAmount = remainingItems.sumOf { it.product.price * it.qty }

        // Return the updated shopping cart with the new total amount
        return updatedCart.copy(
            totalAmount = totalDiscountAmount + remainingItemsAmount
        )
    }

    /**
     * Gets the remaining items in the shopping cart after applying discounts.
     */
    private fun getRemainingItems(shoppingCart: ShoppingCart): List<ShoppingCartItem> {
        return if (shoppingCart.appliedPromotionDiscounts.isNotEmpty()) {
            shoppingCart.appliedPromotionDiscounts.last().remainedItems
        } else {
            shoppingCart.items
        }
    }
}
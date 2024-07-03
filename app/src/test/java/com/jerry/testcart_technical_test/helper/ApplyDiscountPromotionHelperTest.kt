package com.jerry.testcart_technical_test.helper

import com.jerry.testcart_technical_test.data.*
import com.jerry.testcart_technical_test.discountpromotion.*
import com.jerry.testcart_technical_test.models.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class ApplyDiscountPromotionHelperTest {

    private lateinit var helper: ApplyDiscountPromotionHelper

    private val mockShoppingCartItem = listOf(
        mockShoppingCartItemSandwich,
        mockShoppingCartItemBigSandwich,
        mockShoppingCartItemBurger,
        mockShoppingCartItemApple,
        mockShoppingCartItemWaterLemon,
        mockShoppingCartItemCoke,
        mockShoppingCartItemBeer
    )

    private val mockShoppingCart = ShoppingCart(
        items = mockShoppingCartItem
    )

    @BeforeEach
    fun setUp() {
        helper = ApplyDiscountPromotionHelper()
    }


    @Test
    fun `test ApplyDiscountPromotionHelper apply SpecificProductDiscount only with match case return expected result`() = runTest {

        //assign and action
        val actualResult = helper.applyDiscountPromotion(
            shoppingCart = mockShoppingCart,
            discountPromotions = listOf(
                mockSpecificProductDiscount
            )
        )

        //verify
        Assertions.assertEquals(
            1,
            actualResult.appliedPromotionDiscounts.size
        )
        Assertions.assertEquals(
            SpecificProductDiscount::class,
            actualResult.appliedPromotionDiscounts[0].discountPromotion.javaClass.kotlin
        )

        //check the price was applied
        val originalPrice = mockShoppingCartItemApple.product.price * mockShoppingCartItemApple.qty
        val appleDiscountPercentage = 100 - mockSpecificProductDiscountPercentageOff
        val appliedPrice = originalPrice * (appleDiscountPercentage / 100.0)
        Assertions.assertEquals(
            appliedPrice,
            actualResult.appliedPromotionDiscounts.sumOf { it.appliedPrice }
        )

        val sumOfRemainedProductPrice =
            mockShoppingCartItem.filterNot { it == mockShoppingCartItemApple }
                .sumOf { it.product.price * it.qty }
        Assertions.assertEquals(sumOfRemainedProductPrice + appliedPrice, actualResult.totalAmount)
    }

    @Test
    fun `test ApplyDiscountPromotionHelper apply SpecificProductDiscount only without match case return expected result`() = runTest {

    //assign and action
        val actualResult = helper.applyDiscountPromotion(
            shoppingCart = ShoppingCart(
                items = listOf(
                    mockShoppingCartItemBurger
                )
            ),
            discountPromotions = listOf(
                mockSpecificProductDiscount
            )
        )

        //verify
        Assertions.assertEquals(
            0,
            actualResult.appliedPromotionDiscounts.size
        )

        //check the price was applied
        val sumOfBurgerPrice =
            mockShoppingCartItemBurger.product.price * mockShoppingCartItemBurger.qty
        Assertions.assertEquals(sumOfBurgerPrice, actualResult.totalAmount)

    }

    @Test
    fun `test ApplyDiscountPromotionHelper apply BuyGetFreePromotion only with match case return expected result`() = runTest {

    //assign and action
        val actualResult = helper.applyDiscountPromotion(
            shoppingCart = mockShoppingCart,
            discountPromotions = listOf(
                mockBuyGetFreePromotion
            )
        )

        //verify
        Assertions.assertEquals(
            1,
            actualResult.appliedPromotionDiscounts.size
        )
        Assertions.assertEquals(
            BuyGetFreePromotion::class,
            actualResult.appliedPromotionDiscounts[0].discountPromotion.javaClass.kotlin
        )

        //check the price was applied
        Assertions.assertEquals(
            mockBuyGetFreePromotionBuy * mockBuyGetFreePromotionProduct.price,
            actualResult.appliedPromotionDiscounts.sumOf { it.appliedPrice }
        )

        val totalProductPriceExcludingApple =
            mockShoppingCartItem.filterNot { it == mockShoppingCartItemApple }
                .sumOf { it.product.price * it.qty }
        /*
            Item          Unit        price  Total
            ======================================
            Apple            13        1.00  13.00
            Apple 13 for 12   1       -1.00  -1.00  //item added by the "buy 2 get 1 free"
            ======================================
            Total                            12.00
            ...
         */
        val totalPriceOfApple =
            mockShoppingCartItemApple.product.price * (mockShoppingCartItemApple.qty - mockBuyGetFreePromotionFree)
        Assertions.assertEquals(
            totalPriceOfApple + totalProductPriceExcludingApple,
            actualResult.totalAmount
        )

    }

    @Test
    fun `test ApplyDiscountPromotionHelper apply BuyGetFreePromotion only without match case return expected result`() = runTest {
    //assign and action
        val actualResult = helper.applyDiscountPromotion(
            shoppingCart = ShoppingCart(
                items = listOf(
                    mockShoppingCartItemBurger
                )
            ),
            discountPromotions = listOf(
                mockBuyGetFreePromotion
            )
        )

        //verify
        Assertions.assertEquals(
            0,
            actualResult.appliedPromotionDiscounts.size
        )

        //check the price was applied
        val sumOfBurgerPrice =
            mockShoppingCartItemBurger.product.price * mockShoppingCartItemBurger.qty
        Assertions.assertEquals(sumOfBurgerPrice, actualResult.totalAmount)
    }

    @Test
    fun `test ApplyDiscountPromotionHelper apply Multi promotion or discount match case return expected best price result`() = runTest {
    //assign and action
        val actualResult = helper.applyDiscountPromotion(
            shoppingCart = ShoppingCart(
                items = listOf(
                    mockShoppingCartItemApple,
                    mockShoppingCartItemWaterLemon
                )
            ),
            discountPromotions = listOf(
                mockGeneralBasketDiscount,
                mockSpecificProductDiscount
            )
        )

        //verify
        Assertions.assertEquals(
            2,
            actualResult.appliedPromotionDiscounts.size
        )
        Assertions.assertEquals(
            SpecificProductDiscount::class,
            actualResult.appliedPromotionDiscounts[0].discountPromotion.javaClass.kotlin
        )
        Assertions.assertEquals(
            GeneralBasketDiscount::class,
            actualResult.appliedPromotionDiscounts[1].discountPromotion.javaClass.kotlin
        )

        val originalApplePrice = mockShoppingCartItemApple.product.price * mockShoppingCartItemApple.qty
        val discountPercentageApple = 100 - mockSpecificProductDiscountPercentageOff
        val appliedApplePrice = originalApplePrice * (discountPercentageApple / 100.0)
        Assertions.assertEquals(
            appliedApplePrice,
            actualResult.appliedPromotionDiscounts[0].appliedPrice
        )


        Assertions.assertEquals(
            GeneralBasketDiscount::class,
            actualResult.appliedPromotionDiscounts[1].discountPromotion.javaClass.kotlin
        )
        val originalWaterLemonPrice = mockShoppingCartItemWaterLemon.product.price * mockShoppingCartItemWaterLemon.qty
        val discountPercentageWaterLemon = 100 - mockGeneralBasketDiscountPercentageOff
        val appliedWaterLemonPrice = originalWaterLemonPrice * (discountPercentageWaterLemon / 100.0)
        Assertions.assertEquals(
            appliedWaterLemonPrice,
            actualResult.appliedPromotionDiscounts[1].appliedPrice
        )

        //verify total
        Assertions.assertEquals(
            appliedWaterLemonPrice + appliedApplePrice,
            actualResult.totalAmount
        )
    }

    @Test
    fun `test ApplyDiscountPromotionHelper empty list expected return origin price with product`() = runTest {
    //assign and action
        val actualResult = helper.applyDiscountPromotion(
            shoppingCart = ShoppingCart(
                items = listOf(
                    mockShoppingCartItemBurger
                )
            ),
            discountPromotions = listOf()
        )

        //verify
        Assertions.assertEquals(
            0,
            actualResult.appliedPromotionDiscounts.size
        )

        Assertions.assertEquals(
            mockShoppingCartItemBurger.product.price * mockShoppingCartItemBurger.qty,
            actualResult.totalAmount
        )
    }
}
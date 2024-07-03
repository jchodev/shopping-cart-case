package com.jerry.testcart_technical_test.discountpromotion

import com.jerry.testcart_technical_test.data.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

//https://docs.google.com/spreadsheets/d/1bBjfQ2zHCByVQeS-EvMYhOvbkSkRGSoieG8a6BGFJ00/edit?gid=0#gid=0
class BuyGetFreePromotionTest {

    private lateinit var discountPromotion: DiscountPromotion

    @BeforeEach
    fun setUp() {
        discountPromotion = mockBuyGetFreePromotion
    }

    @Test
    fun `test BuyGetFreePromotion contain promotion product, repeatable is false expect return 1 discount promotion applied` (){
        //assign and action
        val actualResult = discountPromotion.apply(
            items = listOf(
                mockShoppingCartItemApple
            )
        )

        //verify
        Assertions.assertEquals(
            1,
            actualResult.size
        )
        //appliedItems
        Assertions.assertEquals (
            1,
            actualResult[0].appliedItems.size
        )
        Assertions.assertEquals (
            mockApple,
            actualResult[0].appliedItems[0].product
        )
        Assertions.assertEquals (
            mockBuyGetFreePromotionBuy + mockBuyGetFreePromotionFree,
            actualResult[0].appliedItems[0].qty
        )

        //remained Items
        Assertions.assertEquals (
            mockApple,
            actualResult[0].remainedItems[0].product
        )
        Assertions.assertEquals (
            mockShoppingCartItemApple.qty - mockBuyGetFreePromotionBuy - mockBuyGetFreePromotionFree,
            actualResult[0].remainedItems[0].qty
        )

        //appliedPrice
        Assertions.assertEquals (
            mockBuyGetFreePromotionProduct.price * mockBuyGetFreePromotionBuy,
            actualResult[0].appliedPrice
        )
    }


    @Test
    fun `test BuyGetFreePromotion was not contain product and repeatable is false expect return 0 discount promotion applied` (){
        //assign and action
        val actualResult = discountPromotion.apply(
            items = listOf(
                mockShoppingCartItemWaterLemon
            )
        )

        //verify
        Assertions.assertEquals(
            0,
            actualResult.size
        )
    }

    @Test
    fun `test BuyGetFreePromotion contain promotion product, repeatable is true expect return more than 1 discount promotion applied` (){
        discountPromotion = BuyGetFreePromotion(
            product = mockBuyGetFreePromotionProduct,
            buy = mockBuyGetFreePromotionBuy,
            free = mockBuyGetFreePromotionFree,
            repeatable = true
        )

        //assign and action
        val actualResult = discountPromotion.apply(
            items = listOf(
                mockShoppingCartItemApple
            )
        )
        //verify
        //count of applied
        Assertions.assertEquals(
            mockShoppingCartItemApple.qty / (mockBuyGetFreePromotionBuy + mockBuyGetFreePromotionFree),
            actualResult.size
        )



        //remained Items
        Assertions.assertEquals (
            mockApple,
            actualResult[0].remainedItems[0].product
        )
        Assertions.assertEquals (
            1,
            actualResult.last().remainedItems[0].qty
        )

        //appliedPrice
        actualResult.forEach {
            Assertions.assertEquals (
                mockBuyGetFreePromotionProduct.price * mockBuyGetFreePromotionBuy,
                it.appliedPrice
            )
        }

    }

    @Test
    fun `test BuyGetFreePromotion was not contain product and repeatable is true expect return 0 discount promotion applied` (){
        discountPromotion = BuyGetFreePromotion(
            product = mockApple,
            buy = mockBuyGetFreePromotionBuy,
            free = mockBuyGetFreePromotionFree,
            repeatable = true
        )

        //assign and action
        val actualResult = discountPromotion.apply(
            items = listOf(
                mockShoppingCartItemWaterLemon
            )
        )

        //verify
        Assertions.assertEquals(
            0,
            actualResult.size
        )
    }

    @Test
    fun `test BuyGetFreePromotion with empty product return 0 discount promotion applied` (){
        discountPromotion = BuyGetFreePromotion(
            product = mockApple,
            buy = mockBuyGetFreePromotionBuy,
            free = mockBuyGetFreePromotionFree,
            repeatable = true
        )

        //assign and action
        val actualResult = discountPromotion.apply(
            items = listOf()
        )

        //verify
        Assertions.assertEquals(
            0,
            actualResult.size
        )
    }

}
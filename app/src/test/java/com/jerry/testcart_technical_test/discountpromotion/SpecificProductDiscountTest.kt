package com.jerry.testcart_technical_test.discountpromotion

import com.jerry.testcart_technical_test.data.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

//https://docs.google.com/spreadsheets/d/1bBjfQ2zHCByVQeS-EvMYhOvbkSkRGSoieG8a6BGFJ00/edit?gid=723315935#gid=723315935
class SpecificProductDiscountTest {

    private lateinit var discountPromotion: DiscountPromotion

    @BeforeEach
    fun setUp() {
        discountPromotion = mockSpecificProductDiscount
    }

    @Test
    fun `test pecificProductDiscount with contain product expect return 1 discount promotion applied`() {

        val shoppingCartList = listOf(
            mockShoppingCartItemApple,
            mockShoppingCartItemWaterLemon,
            mockShoppingCartItemCoke
        )

        //assign and action
        val actualResult = discountPromotion.apply(
            items = shoppingCartList
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

        //remained Items
        Assertions.assertEquals (
            2,
            actualResult[0].remainedItems.size
        )

        //appliedPrice
        Assertions.assertEquals (
            mockShoppingCartItemApple.product.price * mockShoppingCartItemApple.qty * ( (100 - mockSpecificProductDiscountPercentageOff ) / 100),
            actualResult[0].appliedPrice
        )
    }

    @Test
    fun `test pecificProductDiscount without contain product expect return 0 discount promotion applied`() {

        val shoppingCartList = listOf(
            mockShoppingCartItemWaterLemon,
            mockShoppingCartItemCoke
        )

        //assign and action
        val actualResult = discountPromotion.apply(
            items = shoppingCartList
        )

        //verify
        Assertions.assertEquals(
            0,
            actualResult.size
        )
    }

    @Test
    fun `test pecificProductDiscount with empty product item expect return 0 discount promotion applied`() {
        //assign and action
        val actualResult = discountPromotion.apply(
            items = emptyList()
        )

        //verify
        Assertions.assertEquals(
            0,
            actualResult.size
        )
    }
}
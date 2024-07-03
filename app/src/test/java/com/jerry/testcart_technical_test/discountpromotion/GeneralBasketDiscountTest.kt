package com.jerry.testcart_technical_test.discountpromotion

import com.jerry.testcart_technical_test.data.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

//https://docs.google.com/spreadsheets/d/1bBjfQ2zHCByVQeS-EvMYhOvbkSkRGSoieG8a6BGFJ00/edit?gid=61445916#gid=61445916
class GeneralBasketDiscountTest {
    private lateinit var discountPromotion: DiscountPromotion

    @BeforeEach
    fun setUp() {
        discountPromotion = mockGeneralBasketDiscount
    }

    @Test
    fun `test GeneralBasketDiscount with product expect return 1 discount promotion applied`(){

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
            shoppingCartList.size,
            actualResult[0].appliedItems.size
        )

        //remained Items
        Assertions.assertEquals (
            0,
            actualResult[0].remainedItems.size
        )


        //appliedPrice
        Assertions.assertEquals (
            shoppingCartList.sumOf { it.product.price * it.qty } * ( (100 - mockGeneralBasketDiscountPercentageOff ) / 100),
            actualResult[0].appliedPrice
        )
    }

    @Test
    fun `test GeneralBasketDiscount with Empty item expect return 0 discount promotion applied`(){

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
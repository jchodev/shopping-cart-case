package com.jerry.testcart_technical_test.discountpromotion


import com.jerry.testcart_technical_test.data.*
import com.jerry.testcart_technical_test.models.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

//https://docs.google.com/spreadsheets/d/1bBjfQ2zHCByVQeS-EvMYhOvbkSkRGSoieG8a6BGFJ00/edit?gid=293844877#gid=293844877
class CombinationPurchaseDealTest {

    private lateinit var discountPromotion: DiscountPromotion

    @BeforeEach
    fun setUp() {
        discountPromotion = mockCombinationPurchaseDeal
    }

    @Test
    fun `test CombinationPurchaseDeal match deal, Repeatable is false expect return 1 discount promotion applied`() {
        val shoppingCartItem = listOf(
            mockShoppingCartItemApple,
            mockShoppingCartItemCoke,
            mockShoppingCartItemSandwich
        )
        //assign and action
        val actualResult = discountPromotion.apply(
            items = shoppingCartItem
        )

        //verify
        Assertions.assertEquals(
            1,
            actualResult.size
        )

        //appliedItems
        Assertions.assertEquals(
            mockMealDeal.size,
            actualResult[0].appliedItems.size
        )
        actualResult[0].appliedItems.forEach {
            when (it.product) {
                mockApple -> {
                    Assertions.assertEquals(
                        it.qty,
                        mockMealDealCategoryFruit.qty
                    )
                }

                mockSandwich -> {
                    Assertions.assertEquals(
                        it.qty,
                        mockMealDealCategoryFood.qty
                    )
                }

                mockCoke -> {
                    Assertions.assertEquals(
                        it.qty,
                        mockMealDealCategoryFood.qty
                    )
                }
            }
        }

        //remained Items
        Assertions.assertEquals(
            shoppingCartItem.size,
            actualResult[0].remainedItems.count()
        )

        //appliedPrice
        Assertions.assertEquals(
            mockMealDealPrice,
            actualResult[0].appliedPrice
        )
    }

    @Test
    fun `test CombinationPurchaseDeal match deal and get cheaper item for counting, Repeatable is false expect return 1 discount promotion applied`() {
        val shoppingCartItem = listOf(
            mockShoppingCartItemApple,
            mockShoppingCartItemCoke,
            mockShoppingCartItemSandwich,
            mockShoppingCartItemBigSandwich
        )
        //assign and action
        val actualResult = discountPromotion.apply(
            items = shoppingCartItem
        )

        //verify
        Assertions.assertEquals(
            1,
            actualResult.size
        )

        //appliedItems
        Assertions.assertEquals(
            mockMealDeal.size,
            actualResult[0].appliedItems.size
        )
        //check was not count BigSandwich as meal deal
        Assertions.assertEquals(
            0,
            actualResult[0].appliedItems.filter { it.product == mockBigSandwich }.size
        )

        //remained Items
        Assertions.assertEquals(
            shoppingCartItem.size,
            actualResult[0].remainedItems.count()
        )

        //appliedPrice
        Assertions.assertEquals(
            mockMealDealPrice,
            actualResult[0].appliedPrice
        )
    }

    @Test
    fun `test CombinationPurchaseDeal was not match deal expect return 0 discount promotion applied`() {
        //assign and action
        val actualResult = discountPromotion.apply(
            items = listOf(
                mockShoppingCartItemWaterLemon,
                mockShoppingCartItemCoke,
                mockShoppingCartItemBigSandwich
            )
        )

        //verify
        Assertions.assertEquals(
            0,
            actualResult.size
        )
    }

    @Test
    fun `test CombinationPurchaseDeal with empty shopping item return 0 discount promotion applied`() {
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

    @Test
    fun `test CombinationPurchaseDeal match deal, Repeatable is true expect return count of discount promotion larger than 1`() {
        discountPromotion = CombinationPurchaseDeal(
            dealPrice = mockMealDealPrice,
            mealDealCategories = mockMealDeal,
            repeatable = true
        )

        val shoppingCartItem = listOf(
            mockShoppingCartItemApple,
            mockShoppingCartItemCoke,
            mockShoppingCartItemSandwich
        )
        //assign and action
        val actualResult = discountPromotion.apply(
            items = shoppingCartItem
        )
        //verify
        Assertions.assertEquals(
            true,
            actualResult.size > 1
        )

        //appliedPrice
        actualResult.forEach {
            Assertions.assertEquals(
                mockMealDealPrice,
                it.appliedPrice
            )
        }

    }

    @Test
    fun `test CombinationPurchaseDeal was match with two different product with same category`() {

        val mockMealDealCategoryFood = MealDealCategory(mockCategoryFood, 2)
        val mockMealDealCategoryFruit = MealDealCategory(mockCategoryFruit, 1)
        val mockMealDealCategoryDrink = MealDealCategory(mockCategoryDrink, 1)
        val mockMealDeal = listOf(
            mockMealDealCategoryFood,
            mockMealDealCategoryFruit,
            mockMealDealCategoryDrink
        )

        discountPromotion = CombinationPurchaseDeal(
            dealPrice = mockMealDealPrice,
            mealDealCategories = mockMealDeal,
            repeatable = false
        )

        val shoppingCartItem = listOf(
            mockShoppingCartItemApple,
            mockShoppingCartItemCoke,
            ShoppingCartItem(product = mockSandwich, qty = 1),
            ShoppingCartItem(product = mockBigSandwich, qty = 1)
        )

        //assign and action
        val actualResult = discountPromotion.apply(
            items = shoppingCartItem
        )

        //verify
        Assertions.assertEquals(
            1,
            actualResult.size
        )

        //remained Items
        Assertions.assertEquals(
            4,
            actualResult[0].appliedItems.count()
        )

        //remained Items
        Assertions.assertEquals(
            2,
            actualResult[0].remainedItems.count()
        )
        //check was not contain BigSandwich, because it used at meal deal
        Assertions.assertTrue(
            !actualResult[0].remainedItems.any { it.product == mockBigSandwich }
        )
        Assertions.assertTrue(
            !actualResult[0].remainedItems.any { it.product == mockSandwich }
        )

        actualResult[0].remainedItems.forEach {
            if (it.product == mockApple) {
                Assertions.assertEquals(
                    12,
                    it.qty
                )
            } else if (it.product == mockCoke) {
                Assertions.assertEquals(
                    9,
                    it.qty
                )
            }
        }

        //appliedPrice
        Assertions.assertEquals(
            mockMealDealPrice,
            actualResult[0].appliedPrice
        )

    }

    @Test
    fun `test CombinationPurchaseDeal have not enough item to fulfill this meal deal will return 0 applied`() {

        val mockMealDealCategoryFood = MealDealCategory(mockCategoryFood, 3)
        val mockMealDealCategoryFruit = MealDealCategory(mockCategoryFruit, 1)
        val mockMealDealCategoryDrink = MealDealCategory(mockCategoryDrink, 1)
        val mockMealDeal = listOf(
            mockMealDealCategoryFood,
            mockMealDealCategoryFruit,
            mockMealDealCategoryDrink
        )

        discountPromotion = CombinationPurchaseDeal(
            dealPrice = mockMealDealPrice,
            mealDealCategories = mockMealDeal,
            repeatable = false
        )

        val shoppingCartItem = listOf(
            mockShoppingCartItemApple,
            mockShoppingCartItemCoke,
            ShoppingCartItem(product = mockSandwich, qty = 1),
            ShoppingCartItem(product = mockBigSandwich, qty = 1)
        )

        //assign and action
        val actualResult = discountPromotion.apply(
            items = shoppingCartItem
        )

        //verify
        Assertions.assertEquals(
            0,
            actualResult.size
        )
    }

}
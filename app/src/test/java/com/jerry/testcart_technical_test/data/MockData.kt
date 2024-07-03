package com.jerry.testcart_technical_test.data

import com.jerry.testcart_technical_test.discountpromotion.*
import com.jerry.testcart_technical_test.models.*


val mockCategoryFood = Category(
    id = "1",
    name = "Food"
)

val mockCategoryFruit = Category(
    id = "2",
    name = "Fruit"
)

val mockCategoryDrink = Category(
    id = "3",
    name = "Drink"
)

val mockSandwich = Product(
    id = "1",
    name = "Sandwich",
    price = 2.0,
    category = mockCategoryFood,
    supportCombinationPurchaseDeal = true,
)
val mockBigSandwich = Product(
    id = "7",
    name = "BigSandwich",
    price = 3.0,
    category = mockCategoryFood,
    supportCombinationPurchaseDeal = true,
)
val mockApple = Product(
    id = "2",
    name = "Apple",
    price = 1.0,
    category = mockCategoryFruit,
    supportCombinationPurchaseDeal = true,
)
val mockCoke = Product(
    id = "3",
    name = "coke",
    price = 2.0,
    category = mockCategoryDrink,
    supportCombinationPurchaseDeal = true,
)

val mockBurger = Product(
    id = "4",
    name = "Burger",
    price = 3.0,
    category = mockCategoryFood,
    supportCombinationPurchaseDeal = false,
)
val mockWaterLemon = Product(
    id = "5",
    name = "WaterLemon",
    price = 10.0,
    category = mockCategoryFruit,
    supportCombinationPurchaseDeal = false,
)
val mockBeer = Product(
    id = "6",
    name = "Beer",
    price = 2.0,
    category = mockCategoryDrink,
    supportCombinationPurchaseDeal = false,
)

val mockMealDealCategoryFood = MealDealCategory(mockCategoryFood, 1)
val mockMealDealCategoryFruit = MealDealCategory(mockCategoryFruit, 1)
val mockMealDealCategoryDrink = MealDealCategory(mockCategoryDrink, 1)
val mockMealDeal = listOf(
    mockMealDealCategoryFood,
    mockMealDealCategoryFruit,
    mockMealDealCategoryDrink
)
val mockMealDealPrice = 4.0


val mockShoppingCartItemSandwich = ShoppingCartItem(product = mockSandwich, qty = 9)
val mockShoppingCartItemBigSandwich = ShoppingCartItem(product = mockBigSandwich, qty = 11)
val mockShoppingCartItemBurger = ShoppingCartItem(product = mockBurger, qty = 12)
val mockShoppingCartItemApple = ShoppingCartItem(product = mockApple, qty = 13)
val mockShoppingCartItemWaterLemon = ShoppingCartItem(product = mockWaterLemon, qty = 1)
val mockShoppingCartItemCoke = ShoppingCartItem(product = mockCoke, qty = 10)
val mockShoppingCartItemBeer = ShoppingCartItem(product = mockBeer, qty = 10)


//Apple (Buy 2 get 1 free)
val mockBuyGetFreePromotionProduct = mockApple
const val mockBuyGetFreePromotionBuy = 2
const val mockBuyGetFreePromotionFree = 1
val mockBuyGetFreePromotion = BuyGetFreePromotion(
    product = mockBuyGetFreePromotionProduct,
    buy = mockBuyGetFreePromotionBuy,
    free = mockBuyGetFreePromotionFree,
    repeatable = false
)

//10% off total basket
const val mockGeneralBasketDiscountPercentageOff = 10.0
val mockGeneralBasketDiscount = GeneralBasketDiscount(
    percentageOff = mockGeneralBasketDiscountPercentageOff
)

//20% off all apples
const val mockSpecificProductDiscountPercentageOff = 20.0
val mockSpecificProductDiscountProduct = mockApple
val mockSpecificProductDiscount = SpecificProductDiscount(
    product = mockSpecificProductDiscountProduct,
    percentageOff = mockSpecificProductDiscountPercentageOff
)

//meal deal
val mockCombinationPurchaseDeal = CombinationPurchaseDeal(
    dealPrice = mockMealDealPrice,
    mealDealCategories = mockMealDeal,
    repeatable = false
)
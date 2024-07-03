package com.jerry.testcart_technical_test.utils

/**
 * Generates all permutations of the elements in the list.
 *
 * This function mixes up the order of items in a list and return all the possible results.
 *
 */
fun <T> List<T>.permutations(): List<List<T>> = if(isEmpty()) listOf(emptyList()) else  mutableListOf<List<T>>().also{result ->
    for(i in this.indices){
        (this - this[i]).permutations().forEach{
            result.add(it + this[i])
        }
    }
}
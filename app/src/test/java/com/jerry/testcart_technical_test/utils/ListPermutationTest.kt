package com.jerry.testcart_technical_test.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class ListPermutationTest {

    @Test
    fun `test permutations input list expected return all permutations for a list`() {
        val list = listOf("A", "B", "C")
        val expectedPermutations = listOf(
            listOf("A", "B", "C"),
            listOf("A", "C", "B"),
            listOf("B", "A", "C"),
            listOf("B", "C", "A"),
            listOf("C", "A", "B"),
            listOf("C", "B", "A")
        )
        val permutations = list.permutations()
        println(permutations.toSet().sortedBy { it.toString() }) //[[A, B, C], [A, C, B], [B, A, C], [B, C, A], [C, A, B], [C, B, A]]
        println(expectedPermutations.toSet().sortedBy { it.toString() }) //[[A, B, C], [A, C, B], [B, A, C], [B, C, A], [C, A, B], [C, B, A]]
        assertEquals(expectedPermutations.count(), permutations.count())

        assertEquals(
            permutations.toSet().sortedBy { it.toString() },
            expectedPermutations.toSet().sortedBy { it.toString() }
        )
    }
}
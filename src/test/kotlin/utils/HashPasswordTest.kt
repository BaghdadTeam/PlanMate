package utils

import org.baghdad.logic.utils.md5WithSalt
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class HashPasswordTest {
    @Test
    fun `md5WithSalt should return expected hash`() {
        // Given
        val input = "hello"
        val expectedHash = "3c94ed45db499757f41729e3b41d6807" // precomputed MD5("helloBaghdad")
        // When
        val actualHash = input.md5WithSalt()
        // Then
        assertEquals(expectedHash, actualHash)
    }
}
package org.baghdad.logic.usecase.user

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.test.assertEquals
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.repositories.UserRepository

class GetUserByUsernameUseCaseTest {

    private val repository = mockk<UserRepository>(relaxed = true)
    private val useCase   = GetUserByUsernameUseCase(repository)

    private val existingUser = UserEntity(
        name = "Alice",
        username = "alice",
        hashedPassword = "ignored",
        type = UserType.Mate
    )

    @Test
    fun `invoke returns success when user exists`() {
        // Given
        every { repository.findByUsername("alice") } returns existingUser

        // When
        val result = useCase("alice")

        // Then
        assertTrue(result.isSuccess, "Expected success Result")
        val user = result.getOrNull()!!
        assertEquals(existingUser, user)
        verify(exactly = 1) { repository.findByUsername("alice") }
    }

    @Test
    fun `invoke returns failure when user does not exist`() {
        // Given
        every { repository.findByUsername("bob") } returns null

        // When
        val result = useCase("bob")

        // Then
        assertTrue(result.isFailure, "Expected failure Result")
        val ex = result.exceptionOrNull()!!
        assertIs<NoSuchElementException>(ex)
        assertEquals("User 'bob' not found.", ex.message)
        verify(exactly = 1) { repository.findByUsername("bob") }
    }
}

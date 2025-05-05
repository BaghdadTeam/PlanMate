package org.baghdad.logic.usecase.user

import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.user.InvalidUsernameException
import org.baghdad.logic.model.exceptions.user.UserNotFoundException
import org.baghdad.logic.repositories.UserRepository

class GetUserByUsernameUseCaseTest {
    private val repo = mockk<UserRepository>()
    private val uc   = GetUserByUsernameUseCase(repo)
    private val sampleUser = UserEntity(
        name = "Alice",
        username = "alice",
        hashedPassword = "hashed",
        type = UserType.Mate
    )

    @Test
    fun `success for existing`() {
        // Given
        every { repo.findByUsername("alice") } returns sampleUser
        // When
        val user = uc("alice")
        // Then
        assertEquals(sampleUser, user)
    }

    @Test
    fun `throws for blank username`() {
        // When & Then
        assertFailsWith<InvalidUsernameException> {
            uc("")
        }
    }

    @Test
    fun `throws when not found`() {
        // Given
        every { repo.findByUsername("bob") } returns null
        // When & Then
        assertFailsWith<UserNotFoundException> {
            uc("bob")
        }
    }
}

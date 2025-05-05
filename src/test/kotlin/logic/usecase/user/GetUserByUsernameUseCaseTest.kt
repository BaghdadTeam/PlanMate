package org.baghdad.logic.usecase.user

import io.mockk.every
import io.mockk.mockk
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.user.InvalidUsernameException
import org.baghdad.logic.model.exceptions.user.UserNotFoundException
import org.baghdad.logic.repositories.UserRepository

class GetUserByUsernameUseCaseTest {
    private lateinit var repo: UserRepository
    private lateinit var uc: GetUserByUsernameUseCase
    private val sample =
        UserEntity(name = "Alice", username = "alice", hashedPassword = "", type = UserType.Mate)

    @BeforeTest
    fun setup() {
        repo = mockk()
        uc = GetUserByUsernameUseCase(repo)
    }

    @Test
    fun `success when user exists`() {
        // Given
        every { repo.findByUsername("alice") } returns sample
        // When
        val user = uc("alice")
        // Then
        assertEquals(sample, user)
    }

    @Test
    fun `throws for blank username`() {
        assertFailsWith<InvalidUsernameException> {
            uc("")
        }
    }


    @Test
    fun `throws when user not found`() {
        // Given
        every { repo.findByUsername("bob") } throws UserNotFoundException("User 'bob' not found.")
        // Then
        assertFailsWith<UserNotFoundException> {
            uc("bob")
        }
    }

}

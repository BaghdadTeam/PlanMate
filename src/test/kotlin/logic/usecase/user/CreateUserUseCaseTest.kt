package org.baghdad.logic.usecase.user

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.user.*
import org.baghdad.logic.repositories.UserRepository

class CreateUserUseCaseTest {
    private lateinit var repo: UserRepository
    private lateinit var uc: CreateUserUseCase
    private val admin =
        UserEntity(name = "Admin", username = "admin", hashedPassword = "", type = UserType.Admin)
    private val mate =
        UserEntity(name = "Mate", username = "mate", hashedPassword = "", type = UserType.Mate)

    @BeforeTest
    fun setup() {
        repo = mockk(relaxed = true)
        uc = CreateUserUseCase(repo)
    }

    @Test
    fun `success when admin with valid data`() {
        // Given
        every { repo.findByUsername("newUser") } throws UserNotFoundException("not found")
        // When
        val created = uc("newUser", "strongPass", "New Name", admin)
        // Then
        assertEquals("newUser", created.username)
        assertEquals("New Name", created.name)
        assertEquals(UserType.Mate, created.type)
        verify(exactly = 1) { repo.createUser(created) }
    }

    @Test
    fun `throws when creator is not admin`() {
        assertFailsWith<UnauthorizedException> {
            uc("u", "pass", "Name", mate)
        }
    }


    @Test
    fun `throws for blank username`() {
        assertFailsWith<InvalidUsernameException> {
            uc("", "pass", "Name", admin)
        }
    }


    @Test
    fun `throws for username with invalid pattern`() {
        assertFailsWith<InvalidUsernameException> {
            uc("no spaces!", "pass", "Name", admin)
        }
    }


    @Test
    fun `throws for blank name`() {
        assertFailsWith<InvalidNameException> {
            uc("user1", "pass", "", admin)
        }
    }


    @Test
    fun `throws for short password`() {
        assertFailsWith<InvalidPasswordException> {
            uc("user1", "123", "Name", admin)
        }
    }


    @Test
    fun `throws when username already exists`() {
        // Given
        every { repo.findByUsername("dup") } returns UserEntity(
            name = "Dup", username = "dup", hashedPassword = "", type = UserType.Mate
        )
        // Then
        assertFailsWith<UserAlreadyExistsException> {
            uc("dup", "pass", "Name", admin)
        }
    }
}

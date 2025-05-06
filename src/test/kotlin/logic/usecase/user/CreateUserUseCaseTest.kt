package org.baghdad.logic.usecase.user

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.user.*
import org.baghdad.logic.repositories.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CreateUserUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var createUserUseCase: CreateUserUseCase

    private val adminUser = UserEntity(
        name = "Administrator",
        username = "admin",
        hashedPassword = "",
        type = UserType.Admin
    )

    private val regularUser = UserEntity(
        name = "Regular",
        username = "mate",
        hashedPassword = "",
        type = UserType.Mate
    )

    @BeforeEach
    fun setup() {
        userRepository = mockk(relaxed = true)
        createUserUseCase = CreateUserUseCase(userRepository)
    }

    @Test
    fun `should create user when invoked by admin with valid data`() {
        // Given
        every { userRepository.findByUsername("newUser") } throws UserNotFoundException("not found")

        // When
        val created = createUserUseCase("newUser", "strongPassword", "New User", adminUser)

        // Then
        assertEquals("newUser", created.username)
        assertEquals("New User", created.name)
        assertEquals(UserType.Mate, created.type)
        verify(exactly = 1) { userRepository.createUser(created) }
    }

    @Test
    fun `should throw UnauthorizedException when creator is not admin`() {
        // When & Then
        assertFailsWith<UnauthorizedException> {
            createUserUseCase("u", "password", "Name", regularUser)
        }
    }

    @Test
    fun `should throw InvalidUsernameException when username is blank`() {
        // When & Then
        assertFailsWith<InvalidUsernameException> {
            createUserUseCase("", "password", "Name", adminUser)
        }
    }

    @Test
    fun `should throw InvalidUsernameException when username pattern is invalid`() {
        // When & Then
        assertFailsWith<InvalidUsernameException> {
            createUserUseCase("no spaces!", "password", "Name", adminUser)
        }
    }

    @Test
    fun `should throw InvalidNameException when name is blank`() {
        // When & Then
        assertFailsWith<InvalidNameException> {
            createUserUseCase("validUser", "password", "", adminUser)
        }
    }

    @Test
    fun `should throw InvalidPasswordException when password is too short`() {
        // When & Then
        assertFailsWith<InvalidPasswordException> {
            createUserUseCase("validUser", "123", "Name", adminUser)
        }
    }

    @Test
    fun `should throw UserAlreadyExistsException when username already exists`() {
        // Given
        every { userRepository.findByUsername("existingUser") } returns UserEntity(
            name = "Exists",
            username = "existingUser",
            hashedPassword = "",
            type = UserType.Mate
        )

        // When & Then
        assertFailsWith<UserAlreadyExistsException> {
            createUserUseCase("existingUser", "password", "Name", adminUser)
        }
    }
}

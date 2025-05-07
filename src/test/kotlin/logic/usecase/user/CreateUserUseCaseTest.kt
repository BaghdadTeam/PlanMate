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
import org.junit.jupiter.api.assertThrows
import com.google.common.truth.Truth.assertThat

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
        every { userRepository.getUserByUsername("newUser") } throws UserNotFoundException("not found")

        val created = createUserUseCase("newUser", "strongPassword", "New User", adminUser)

        assertThat(created.username).isEqualTo("newUser")
        assertThat(created.name).isEqualTo("New User")
        assertThat(created.type).isEqualTo(UserType.Mate)
        verify(exactly = 1) { userRepository.createUser(created) }
    }

    @Test
    fun `should throw UnauthorizedException when creator is not admin`() {
        val exception = assertThrows<UnauthorizedException> {
            createUserUseCase("u", "password", "Name", regularUser)
        }
        assertThat(exception).isInstanceOf(UnauthorizedException::class.java)
    }

    @Test
    fun `should throw InvalidUsernameException when username is blank`() {
        val exception = assertThrows<InvalidUsernameException> {
            createUserUseCase("", "password", "Name", adminUser)
        }
        assertThat(exception).isInstanceOf(InvalidUsernameException::class.java)
    }

    @Test
    fun `should throw InvalidUsernameException when username pattern is invalid`() {
        val exception = assertThrows<InvalidUsernameException> {
            createUserUseCase("no spaces!", "password", "Name", adminUser)
        }
        assertThat(exception).isInstanceOf(InvalidUsernameException::class.java)
    }

    @Test
    fun `should throw InvalidNameException when name is blank`() {
        val exception = assertThrows<InvalidNameException> {
            createUserUseCase("validUser", "password", "", adminUser)
        }
        assertThat(exception).isInstanceOf(InvalidNameException::class.java)
    }

    @Test
    fun `should throw InvalidPasswordException when password is too short`() {
        val exception = assertThrows<InvalidPasswordException> {
            createUserUseCase("validUser", "123", "Name", adminUser)
        }
        assertThat(exception).isInstanceOf(InvalidPasswordException::class.java)
    }

    @Test
    fun `should throw UserAlreadyExistsException when username already exists`() {
        every { userRepository.getUserByUsername("existingUser") } returns UserEntity(
            name = "Exists",
            username = "existingUser",
            hashedPassword = "",
            type = UserType.Mate
        )

        val exception = assertThrows<UserAlreadyExistsException> {
            createUserUseCase("existingUser", "password", "Name", adminUser)
        }
        assertThat(exception).isInstanceOf(UserAlreadyExistsException::class.java)
    }
}

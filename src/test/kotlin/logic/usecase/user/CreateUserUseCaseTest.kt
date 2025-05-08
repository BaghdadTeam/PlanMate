package org.baghdad.logic.usecase.user

import com.google.common.truth.Truth.assertThat
import helpers.authentication.createUserHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.user.InvalidNameException
import org.baghdad.logic.model.exceptions.user.InvalidPasswordException
import org.baghdad.logic.model.exceptions.user.InvalidUsernameException
import org.baghdad.logic.model.exceptions.user.UnauthorizedException
import org.baghdad.logic.model.exceptions.user.UserAlreadyExistsException
import org.baghdad.logic.model.exceptions.user.UserNotFoundException
import org.baghdad.logic.repositories.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CreateUserUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var createUserUseCase: CreateUserUseCase
    private lateinit var userValidatorUseCase: UserValidatorUseCase

    private val adminUser = createUserHelper()
    private val mateUser = createUserHelper().copy(type = UserType.Mate)


    @BeforeEach
    fun setup() {
        userRepository = mockk(relaxed = true)
        createUserUseCase = mockk(relaxed = true)
        userValidatorUseCase = mockk(relaxed = true)
        createUserUseCase = CreateUserUseCase(userRepository, userValidatorUseCase)
    }

    @Test
    fun `should create user when invoked by admin with valid data`() {
        every { userRepository.getUserByUsername("newUser") } throws UserNotFoundException("not found")
        every { userValidatorUseCase.invoke(any(), any(), any(), any()) } returns Unit
        val created = createUserUseCase("newUser", "strongPassword", "New User", adminUser.id)

        assertThat(created.username).isEqualTo("newUser")
        assertThat(created.name).isEqualTo("New User")
        assertThat(created.type).isEqualTo(UserType.Mate)
        verify(exactly = 1) { userRepository.createUser(created) }
    }


}

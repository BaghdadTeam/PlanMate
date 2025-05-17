package org.baghdad.logic.usecase.user

import com.google.common.truth.Truth.assertThat
import helpers.authentication.createUserHelper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.UserNotFoundException
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.utils.md5WithSalt
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CreateUserUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var createUserUseCase: CreateUserUseCase
    private lateinit var userValidatorUseCase: UserValidatorUseCase
    private val sessionManager: SessionManager = mockk(relaxUnitFun = true)

    private val adminUser = createUserHelper()


    @BeforeEach
    fun setup() {
        userRepository = mockk(relaxed = true)
        createUserUseCase = mockk(relaxed = true)
        userValidatorUseCase = mockk(relaxed = true)
        createUserUseCase = CreateUserUseCase(userRepository, userValidatorUseCase, sessionManager)
        coEvery { sessionManager.isAuthenticated() } returns true
    }

    @Test
    fun `should create user when invoked by admin with valid data`() = runTest {
        coEvery { userRepository.getUserByUsername("newUser") } throws UserNotFoundException("not found")
        coEvery { userValidatorUseCase.invoke(any(), any(), any()) } returns Unit
        val created = createUserUseCase("newUser", "hashedPassword", "New User", adminUser.id)

        assertThat(created.username).isEqualTo("newUser")
        assertThat(created.name).isEqualTo("New User")
        assertThat(created.type).isEqualTo(UserType.Mate)
        coVerify(exactly = 1) { userRepository.createUser(created, hashedPassword = "hashedPassword".md5WithSalt()) }
    }


}

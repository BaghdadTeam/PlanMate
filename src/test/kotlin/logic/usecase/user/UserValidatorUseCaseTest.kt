package logic.usecase.user

import helpers.authentication.createUserHelper
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.model.exceptions.user.InvalidNameException
import org.baghdad.logic.model.exceptions.user.InvalidPasswordException
import org.baghdad.logic.model.exceptions.user.InvalidUsernameException
import org.baghdad.logic.model.exceptions.user.UserAlreadyExistsException
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.usecase.admin.AdminPermissionCheckerUseCase
import org.baghdad.logic.usecase.user.UserValidatorUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserValidatorUseCaseTest {


    private companion object {
        const val TEST_USERNAME = "aboud"
        const val TEST_NAME = "Test User"
    }


    private lateinit var userRepository: UserRepository
    private lateinit var userValidatorUseCase: UserValidatorUseCase
    private lateinit var isAdmin: AdminPermissionCheckerUseCase


    @BeforeEach
    fun setup() {
        userRepository = mockk()
        isAdmin = AdminPermissionCheckerUseCase(userRepository)
        userValidatorUseCase = UserValidatorUseCase(userRepository, isAdmin)
    }

    @Test
    fun `should not throw any exception when validate user data`() = runTest {
        // Given
        val user = createUserHelper()
        coEvery { userRepository.isUsernameTaken(TEST_USERNAME) } returns false
        coEvery { userRepository.getUserById(user.id) } returns user

        // When & Then
        userValidatorUseCase.invoke(TEST_USERNAME, user.hashedPassword, user.name, user.id)

    }

    @Test
    fun `should throw exception when username is blank`() = runTest {
        // Given
        val user = createUserHelper()
        coEvery { userRepository.isUsernameTaken(TEST_USERNAME) } returns false
        coEvery { userRepository.getUserById(user.id) } returns user

        // When & Then
        assertThrows<InvalidUsernameException> {
            userValidatorUseCase.invoke(
                "",
                user.hashedPassword,
                user.name,
                user.id
            )
        }
    }

    @Test
    fun `should throw UserAlreadyExistsException when username is not unique`() = runTest {
        // Given
        val user = createUserHelper().copy(username = TEST_USERNAME)
        coEvery { userRepository.getUserById(user.id) } returns user
        coEvery { userRepository.isUsernameTaken(TEST_USERNAME) } returns true

        // When & Then
        assertThrows<UserAlreadyExistsException> {
            userValidatorUseCase.invoke(
                TEST_USERNAME,
                user.hashedPassword,
                user.name,
                user.id
            )
        }

    }



    @Test
    fun `should throw InvalidNameException when name is blank`() = runTest {
        // Given
        val user = createUserHelper()
        coEvery { userRepository.getUserById(user.id) } returns user
        coEvery { userRepository.isUsernameTaken(TEST_USERNAME) } returns false
        // When & Then
        assertThrows<InvalidNameException> {
            userValidatorUseCase.invoke(
                TEST_USERNAME,
                user.hashedPassword,
                "",
                user.id
            )
        }

    }

    @Test
    fun `should throw InvalidPasswordException when password is blank`() = runTest {
        // Given
        val user = createUserHelper().copy(hashedPassword = "")
        coEvery { userRepository.getUserById(user.id) } returns user
        coEvery { userRepository.isUsernameTaken(TEST_USERNAME) } returns false
        // When & Then
        assertThrows<InvalidPasswordException> {
            userValidatorUseCase.invoke(
                TEST_USERNAME,
                user.hashedPassword,
                TEST_USERNAME,
                user.id
            )
        }
    }

    @Test
    fun `should throw InvalidPasswordException when password is too short`() = runTest {
        // Given
        val user = createUserHelper().copy(hashedPassword = "12")
        coEvery { userRepository.getUserById(user.id) } returns user
        coEvery { userRepository.isUsernameTaken(TEST_USERNAME) } returns false

        // when & then
        assertThrows<InvalidPasswordException> {
            userValidatorUseCase.invoke(
                TEST_USERNAME,
                user.hashedPassword,
                TEST_USERNAME,
                user.id
            )
        }
    }

    @Test
    fun `should throw InvalidNameException when username is too short`() = runTest {
        // Given
        val user = createUserHelper().copy(name = "a")
        coEvery { userRepository.getUserById(user.id) } returns user
        coEvery { userRepository.isUsernameTaken(TEST_USERNAME) } returns false
        // When & Then
        assertThrows<InvalidUsernameException> {
            userValidatorUseCase.invoke(
                "a",
                user.hashedPassword,
                TEST_USERNAME,
                user.id
            )
        }
    }

}

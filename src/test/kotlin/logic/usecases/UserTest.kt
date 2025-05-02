package logic.usecases

import helpers.task.createUserHelper
import io.mockk.mockk
import org.baghdad.logic.repositories.UserRepository
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class UserTest {
    private lateinit var userService: UserService
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp() {
        userRepository = mockk()
        userService = UserService(userRepository)
    }

    @Test
    fun `Should create user successfully if admin`() {
        // Given
        val admin = createUserHelper()
        val newUser = User("mate1", "123", Role.MATE)
        every { userRepository.exists("mate1") } returns false
        every { userRepository.saveUser(newUser) } just Runs

        // When
        val result = userService.createUser(admin, "mate1", "123", Role.MATE)

        // Then
        assertThat(result).isEqualTo(newUser)
        verify { userRepository.saveUser(newUser) }
    }

    @Test
    fun `Should throw exception if username already exists`() {
        // Given
        val admin = createUserHelper()
        every { userRepository.exists("mate1") } returns true

        // When & Then
        org.junit.jupiter.api.assertThrows<DuplicateUsernameException> {
            userService.createUser(admin, "mate1", "123", Role.MATE)
        }
    }

    @Test
    fun `Should throw exception if non-admin tries to create user`() {
        // Given
        val nonAdmin = createUserHelper(username = "mate", role = Role.MATE)
        // When & Then
        org.junit.jupiter.api.assertThrows<UnauthorizedException> {
            userService.createUser(nonAdmin, "user2", "123", Role.MATE)
        }
    }
}
package logic.usecases

import helpers.task.createUserHelper
import io.mockk.mockk
import org.baghdad.logic.repositories.UserRepository
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class UserUiTest {
    private lateinit var userService: UserService
    private lateinit var userRepository: UserRepository
    private lateinit var userUi: UserUi

    @BeforeEach
    fun setUp() {
        userRepository = mockk()
        userService = UserService(userRepository)
        userUi = UserUi(userService)
    }

    @Test
    fun `Should simulate admin user creation flow`() {
        // Given
        val admin = createUserHelper()
        every { userRepository.exists("newmate") } returns false
        every { userRepository.saveUser(any()) } just Runs

        System.setIn(fakeInputStream("newmate", "1234", "MATE"))

        val output = captureOutput {
            userUi.createUserCommand(admin)
        }

        assertThat(output).contains("User created successfully")
    }

    @Test
    fun `Should display error when non-admin tries to create user`() {
        // Given
        val nonAdmin = createUserHelper(role = Role.MATE)

        val output = captureOutput {
            userUi.createUserCommand(nonAdmin)
        }

        assertThat(output).contains("Only admin can create users")
    }
}
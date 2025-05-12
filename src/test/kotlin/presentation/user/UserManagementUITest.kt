package presentation.user

import org.baghdad.presentation.user.CreateUserUI
import org.baghdad.presentation.user.GetUserByUsernameUI
import org.baghdad.presentation.user.UserManagementUI
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class UserManagementUITest {

    private lateinit var reader: Reader
    private lateinit var viewer: Viewer
    private lateinit var session: SessionManager
    private lateinit var userRepository: UserRepository
    private lateinit var createUserUI: CreateUserUI
    private lateinit var getUserByUsernameUI: GetUserByUsernameUI
    private lateinit var userManagementUI: UserManagementUI

    private val adminId = UUID.randomUUID()
    private val adminUser = UserEntity(
        id = adminId,
        username = "admin",
        name = "Admin User",
        hashedPassword = "hashed",
        type = UserType.Admin
    )

    @BeforeEach
    fun setUp() {
        reader = mockk()
        viewer = mockk(relaxUnitFun = true)
        session = mockk()
        userRepository = mockk()
        createUserUI = mockk()
        getUserByUsernameUI = mockk()

        every { session.currentSession.userId } returns adminId
        coEvery { userRepository.getUserById(adminId) } returns adminUser

        userManagementUI = UserManagementUI(
            reader, viewer, session, userRepository, createUserUI, getUserByUsernameUI
        )
    }

    @Test
    fun `should show unauthorized error when user is not admin`() = runBlocking {
        val nonAdminId = UUID.randomUUID()
        every { session.currentSession.userId } returns nonAdminId
        coEvery { userRepository.getUserById(nonAdminId) } returns adminUser.copy(type = UserType.Mate)

        userManagementUI.invoke()

        verify { viewer.logError("Unauthorized: Only admins can access user management.") }
    }

    @Test
    fun `should create user when option 1 is selected`() = runBlocking {
        every { reader.readInput() } returnsMany listOf("1", "0")
        coEvery { createUserUI.invoke() } just Runs

        userManagementUI.invoke()

        coVerify { createUserUI.invoke() }
    }

    @Test
    fun `should call getUserByUsernameUI when option 2 is selected`() = runBlocking {
        every { reader.readInput() } returnsMany listOf("2", "0")
        coEvery { getUserByUsernameUI.run() } just Runs

        userManagementUI.invoke()

        coVerify { getUserByUsernameUI.run() }
    }

    @Test
    fun `should print error on invalid option`() = runBlocking {
        every { reader.readInput() } returnsMany listOf("99", "0")

        userManagementUI.invoke()

        verify { viewer.logError("Invalid choice. Please try again.") }
    }

    @Test
    fun `should trim input before processing`() = runBlocking {
        every { reader.readInput() } returnsMany listOf(" 1  ", "  0 ")
        coEvery { createUserUI.invoke() } just Runs

        userManagementUI.invoke()

        coVerify { createUserUI.invoke() }
    }
}

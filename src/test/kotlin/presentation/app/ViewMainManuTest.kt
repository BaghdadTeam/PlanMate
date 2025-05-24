package presentation.app

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.usecase.admin.AdminPermissionCheckerUseCase
import org.baghdad.presentation.app.ViewMainManu
import org.baghdad.presentation.authentication.LogoutUi
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.project.ProjectManagementUI
import org.baghdad.presentation.swimlane.SwimlaneUI
import org.baghdad.presentation.user.CreateUserUI
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class ViewMainManuTest {
    private lateinit var projectManagementUI: ProjectManagementUI
    private lateinit var createUserUI: CreateUserUI
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var swimlaneUI: SwimlaneUI
    private lateinit var adminPermissionCheckerUseCase: AdminPermissionCheckerUseCase
    private lateinit var session: SessionManager
    private lateinit var logoutUI: LogoutUi
    private lateinit var viewMainManu: ViewMainManu

    @BeforeEach
    fun setUp() {
        projectManagementUI = mockk()
        createUserUI = mockk()
        viewer = mockk()
        reader = mockk()
        swimlaneUI = mockk()
        adminPermissionCheckerUseCase = mockk()
        session = mockk()
        logoutUI = mockk()

        viewMainManu = ViewMainManu(
            projectManagementUI,
            createUserUI,
            viewer,
            reader,
            swimlaneUI,
            adminPermissionCheckerUseCase,
            session,
            logoutUI
        )

        coEvery { viewer.logMessage(any()) } just runs
        coEvery { viewer.logError(any()) } just runs
        coEvery { viewer.log(any()) } just runs

    }


    @Test
    fun `should display main menu and handle invalid input`() = runTest {
        // Given
        val userId = UUID.randomUUID()

        coEvery { session.currentSession.userId } returns userId
        coEvery { adminPermissionCheckerUseCase(userId) } returns true

        every { reader.readInput() } returns "123" andThen "0"

        // When
        viewMainManu()

        // Then
        verifySequence() {
            viewer.logMessage("=== Main Menu ===")
            viewer.logMessage("1. Project Management")
            viewer.logMessage("2. Logout")
            viewer.logMessage("3. User Management")
            viewer.logMessage("0. Exit")
            viewer.log("Enter your choice: ")
            viewer.logMessage("Invalid choice. Please try again.")

            viewer.logMessage("=== Main Menu ===")
            viewer.logMessage("1. Project Management")
            viewer.logMessage("2. Logout")
            viewer.logMessage("3. User Management")
            viewer.logMessage("0. Exit")
            viewer.log("Enter your choice: ")
            viewer.logMessage("Goodbye!")
        }
    }

    @Test
    fun `should handle project management`() = runTest {
        // Given
        val userId = UUID.randomUUID()
        val project = Pair(UUID.randomUUID(), "Project 1")
        coEvery { session.currentSession.userId } returns userId
        coEvery { adminPermissionCheckerUseCase(userId) } returns true
        coEvery { projectManagementUI.invoke() } returns project
        coEvery { swimlaneUI.invoke(project) } just runs
        every { reader.readInput() } returns "1" andThen "0"

        // When
        viewMainManu()

        // Then
        coVerify(exactly = 1) {
            swimlaneUI.invoke(project)
        }
    }

    @Test
    fun `should handle logout`() = runTest {
        // Given
        val userId = UUID.randomUUID()
        coEvery { session.currentSession.userId } returns userId
        coEvery { adminPermissionCheckerUseCase(userId) } returns true
        every { logoutUI.execute() } just runs
        every { reader.readInput() } returns "2" andThen "0"

        // When
        viewMainManu()

        // Then
        verify(exactly = 1) {
            logoutUI.execute()
        }
    }

    @Test
    fun `should handle user creation`() = runTest {
        // Given
        val userId = UUID.randomUUID()
        coEvery { session.currentSession.userId } returns userId
        coEvery { adminPermissionCheckerUseCase(userId) } returns true
        coEvery { createUserUI.invoke() } just runs
        every { reader.readInput() } returns "3" andThen "0"

        // When
        viewMainManu()

        // Then
        coVerify(exactly = 1) {
            createUserUI.invoke()
        }
    }

    @Test
    fun `should not execute swimlaneUI when project is null`() = runTest {
        // Given
        val userId = UUID.randomUUID()
        val project = null
        coEvery { session.currentSession.userId } returns userId
        coEvery { adminPermissionCheckerUseCase(userId) } returns true
        coEvery { projectManagementUI.invoke() } returns project
        every { reader.readInput() } returns "1" andThen "0"
        // When
        viewMainManu()
        // Then
        coVerify(exactly = 0) {
            swimlaneUI.invoke(mockk())
        }
    }

    @Test
    fun `should not view user management when user is not admin and if input is 3 should view logError`() =
        runTest {
            // Given
            val userId = UUID.randomUUID()
            coEvery { session.currentSession.userId } returns userId
            coEvery { adminPermissionCheckerUseCase(userId) } returns false
            every { reader.readInput() } returns "3" andThen "0"

            // When
            viewMainManu()

            // Then
            verifySequence {
                viewer.logMessage("=== Main Menu ===")
                viewer.logMessage("1. Project Management")
                viewer.logMessage("2. Logout")
                viewer.logMessage("0. Exit")
                viewer.log("Enter your choice: ")
                viewer.logMessage("Invalid choice. Please try again.")

                viewer.logMessage("=== Main Menu ===")
                viewer.logMessage("1. Project Management")
                viewer.logMessage("2. Logout")
                viewer.logMessage("0. Exit")
                viewer.log("Enter your choice: ")
                viewer.logMessage("Goodbye!")
            }
            coVerify(exactly = 0){
                createUserUI.invoke()
            }
        }
}




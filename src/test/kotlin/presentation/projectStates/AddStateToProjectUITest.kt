package presentation.projectStates

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.usecase.projectstates.AddStateToProjectUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.projectStates.AddStateToProjectUI
import org.junit.jupiter.api.BeforeEach
import java.util.UUID
import kotlin.test.Test


class AddStateToProjectUITest {

    private lateinit var useCase: AddStateToProjectUseCase
    private lateinit var sessionManager: SessionManager
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var ui: AddStateToProjectUI

    private val fakeSession = mockk<SessionEntity>(relaxed = true)
    private val userId = UUID.randomUUID().toString()

    @BeforeEach
    fun setup() {
        useCase = mockk(relaxed = true)
        sessionManager = mockk()
        viewer = mockk(relaxed = true)
        reader = mockk()
        every { fakeSession.userId } returns userId
        every { sessionManager.currentSession } returns fakeSession

        ui = AddStateToProjectUI(useCase, sessionManager, viewer, reader)
    }

    @Test
    fun `execute should call useCase with correct state`() {
        val stateName = "To Do"
        val projectId = "project-123"

        every { reader.readInput() } returnsMany listOf(stateName, projectId)

        ui.execute()

        verify {
            useCase.invoke(
                match {
                    it.name == stateName &&
                            it.projectId == projectId &&
                            it.creatorId == userId
                },
                UUID.fromString(userId)
            )
        }
        verify { viewer.logMessage("State 'To Do' added to project successfully.") }
    }

    @Test
    fun `promptForStateName should loop until valid name is provided`() {
        every { reader.readInput() } returnsMany listOf("", "  ", "Done")
        val method = ui.javaClass.getDeclaredMethod("promptForStateName")
        method.isAccessible = true
        val result = method.invoke(ui) as String
        assertThat(result).isEqualTo("Done")
        verify(exactly = 2) { viewer.logError("State name cannot be blank. Please try again.") }
    }

    @Test
    fun `promptForProjectId should loop until valid id is provided`() {
        every { reader.readInput() } returnsMany listOf("", null, "project-456")
        val method = ui.javaClass.getDeclaredMethod("promptForProjectId")
        method.isAccessible = true
        val result = method.invoke(ui) as String
        assertThat(result).isEqualTo("project-456")
        verify(exactly = 2) { viewer.logError("Project ID cannot be blank. Please try again.") }
    }

    @Test
    fun `tryAddState should show error message when useCase throws exception`() {
        val state = StateEntity(name = "Done", projectId = "project-123", creatorId = userId)
        val uuid = UUID.fromString(userId)
        every { useCase.invoke(any(), any()) } throws RuntimeException("Something went wrong")

        val method = ui.javaClass.getDeclaredMethod("tryAddState", StateEntity::class.java, UUID::class.java)
        method.isAccessible = true
        method.invoke(ui, state, uuid)

        verify { viewer.logError("Failed to add state: Something went wrong") }
    }
}

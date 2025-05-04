package presentation.projectStates

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.usecase.projectstates.DeleteStateForProjectUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.projectStates.DeleteStateForProjectUI
import org.junit.jupiter.api.BeforeEach
import java.util.UUID
import kotlin.test.Test

class DeleteStateForProjectUITest {

    private lateinit var useCase: DeleteStateForProjectUseCase
    private lateinit var sessionManager: SessionManager
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var ui: DeleteStateForProjectUI
    private val session = mockk<SessionEntity>()
    private val userId = UUID.randomUUID()

    @BeforeEach
    fun setUp() {
        useCase = mockk(relaxed = true)
        sessionManager = mockk()
        viewer = mockk(relaxed = true)
        reader = mockk()

        every { session.userId } returns userId.toString()
        every { sessionManager.currentSession } returns session

        ui = DeleteStateForProjectUI(useCase, sessionManager, viewer, reader)
    }

    @Test
    fun `execute should call useCase with correct values`() {
        val stateId = UUID.randomUUID()
        every { reader.readInput() } returns stateId.toString()

        ui.execute()

        verify {
            useCase.invoke(stateId, userId)
        }
        verify { viewer.logMessage("State deleted successfully.") }
    }

    @Test
    fun `promptForStateId should retry on blank input and succeed`() {
        every { reader.readInput() } returnsMany listOf("", " ", "valid-id")

        val method = ui.javaClass.getDeclaredMethod("promptForStateId")
        method.isAccessible = true
        val result = method.invoke(ui) as String

        assertThat(result).isEqualTo("valid-id")
        verify(exactly = 2) { viewer.logError("State ID cannot be blank. Please try again.") }
    }

    @Test
    fun `tryDeleteState should log error on exception`() {
        val method = ui.javaClass.getDeclaredMethod("tryDeleteState", UUID::class.java, UUID::class.java)
        method.isAccessible = true

        every { useCase.invoke(any(), any()) } throws Exception("State not found")

        method.invoke(ui, UUID.randomUUID(), userId)

        verify { viewer.logError("Failed to delete state: State not found") }
    }
}

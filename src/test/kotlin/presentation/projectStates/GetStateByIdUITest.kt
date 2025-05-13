package presentation.projectStates

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.model.entities.TaskStateEntity
import org.baghdad.logic.usecase.projectstates.GetStateByIdUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.projectStates.GetStateByIdUI
import org.junit.jupiter.api.BeforeEach
import java.util.*
import kotlin.test.Test

class GetStateByIdUITest {

    private lateinit var useCase: GetStateByIdUseCase
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var getStateByIdUI: GetStateByIdUI


    @BeforeEach
    fun setUp() {
        useCase = mockk()
        viewer = mockk(relaxed = true)
        reader = mockk()
        getStateByIdUI = GetStateByIdUI(useCase, viewer, reader)
    }

    @Test
    fun `should display state name when found`() {
        val state = TaskStateEntity(name = "In Progress", projectId = UUID.randomUUID(), creatorId = UUID.randomUUID())

        every { reader.readInput() } returns state.id.toString()
        coEvery { useCase.invoke(state.id) } returns state

        getStateByIdUI.execute()

        verify { viewer.logMessage("State: In Progress") }
    }

    @Test
    fun `should show error when state is not found`() {
        val stateId = UUID.randomUUID()

        every { reader.readInput() } returns stateId.toString()
        coEvery { useCase.invoke(stateId) } throws Exception("No state found")

        getStateByIdUI.execute()

        verify { viewer.logError("Failed to retrieve state: No state found") }
    }

    @Test
    fun `should retry when input is blank`() {
        val id = UUID.randomUUID()
        every { reader.readInput() } returnsMany listOf("", "  ", "$id")
        coEvery { useCase.invoke(id) } returns TaskStateEntity(
            id = id, name = "To Do",
            projectId = UUID.randomUUID(),
            creatorId = UUID.randomUUID()
        )

        getStateByIdUI.execute()

        verify(exactly = 2) { viewer.logError("State ID cannot be blank. Please try again.") }
        verify { viewer.logMessage("State: To Do") }
    }
}

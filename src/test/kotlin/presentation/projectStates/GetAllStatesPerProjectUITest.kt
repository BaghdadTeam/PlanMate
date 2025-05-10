package presentation.projectStates

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.usecase.projectstates.GetAllStatesPerProjectUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.projectStates.GetAllStatesPerProjectUI
import org.junit.jupiter.api.BeforeEach
import java.util.*
import kotlin.test.Test

class GetAllStatesPerProjectUITest {

    private lateinit var useCase: GetAllStatesPerProjectUseCase
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var getAllStatesPerProjectUI: GetAllStatesPerProjectUI

    @BeforeEach
    fun setUp() {
        useCase = mockk()
        viewer = mockk(relaxed = true)
        reader = mockk()
        getAllStatesPerProjectUI = GetAllStatesPerProjectUI(useCase, viewer)
    }

    @Test
    fun `test valid project ID with multiple states`() {
        val projectId = UUID.randomUUID()
        val creatorId = UUID.randomUUID()
        val states = listOf(
            StateEntity(name = "Backlog", projectId = projectId, creatorId = creatorId),
            StateEntity(name = "In Progress", projectId = projectId, creatorId = creatorId),
            StateEntity(name = "Done", projectId = projectId, creatorId = creatorId)
        )

        every { reader.readInput() } returns projectId.toString()
        coEvery { useCase.invoke(projectId) } returns states

        getAllStatesPerProjectUI.execute(projectId)

        verify { viewer.logMessage("1. Backlog") }
        verify { viewer.logMessage("2. In Progress") }
        verify { viewer.logMessage("3. Done") }
    }

    @Test
    fun `test valid project ID with no states`() {
        val projectId = UUID.randomUUID()
        every { reader.readInput() } returns projectId.toString()
        coEvery { useCase.invoke(projectId) } returns emptyList()

        getAllStatesPerProjectUI.execute(projectId)

        verify { viewer.logMessage("No states found for this project") }
    }

    @Test
    fun `test blank input retried until valid project ID`() {
        val projectId = UUID.randomUUID()

        every { reader.readInput() } returnsMany listOf("", "  ", projectId.toString())
        coEvery { useCase.invoke(projectId) } returns emptyList()

        getAllStatesPerProjectUI.execute(projectId)

        verify { viewer.logMessage("No states found for this project") }
    }

    @Test
    fun `test exception is handled during use case invocation`() {
        val projectId = UUID.randomUUID()
        every { reader.readInput() } returns projectId.toString()
        coEvery { useCase.invoke(projectId) } throws RuntimeException("Something went wrong")

        getAllStatesPerProjectUI.execute(projectId)

        verify { viewer.logError("Failed to retrieve states: Something went wrong") }
    }
}
package presentation.project

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.coroutines.runBlocking
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.usecase.project.EditProjectUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.project.EditProjectUi
import org.baghdad.presentation.project.GetAllProjectsUi
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class EditProjectUITest {

    private lateinit var editProjectUi: EditProjectUi
    private lateinit var editProjectUseCase: EditProjectUseCase
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var session: SessionManager
    private lateinit var getAllProjectsUi: GetAllProjectsUi

    @BeforeEach
    fun setUp() {
        viewer = mockk(relaxed = true)
        reader = mockk(relaxed = true)
        session = mockk(relaxed = true)
        getAllProjectsUi = mockk()
        editProjectUseCase = mockk()
        editProjectUi = EditProjectUi(editProjectUseCase, session, viewer, getAllProjectsUi, reader)

        every { viewer.logMessage(any()) } just Runs
        every { viewer.logError(any()) } just Runs
    }

    @Test
    fun `should edit project successfully`() = runBlocking {
        // Given
        val projects =
            listOf(UUID.randomUUID(), UUID.randomUUID()) to listOf("Project 1", "Project 2")
        val userId = UUID.randomUUID()
        val projectNumber = 1
        val newProjectName = "New Project Name"

        // when
        coEvery { session.currentSession.userId } returns userId
        coEvery { getAllProjectsUi() } returns projects
        coEvery { reader.readInput() } returns projectNumber.toString() andThen newProjectName
        coEvery {
            editProjectUseCase(
                projects.first[projectNumber - 1],
                newProjectName,
                userId
            )
        } returns Unit

        // Act
        editProjectUi.editProject()

        // Then
        verify { viewer.logMessage("=== Edit Project ===") }
        verify { viewer.logMessage("=== View Project ===") }
        verify { viewer.log("Enter project Number: ") }
        verify { viewer.log("Enter new project name: ") }
        coVerify { editProjectUseCase(projects.first[projectNumber - 1], newProjectName, userId) }
    }

    @Test
    fun `should log error when project id is not a number and continue ask the user for a number`() =
        runBlocking {
            // Given
            val projects =
                listOf(UUID.randomUUID(), UUID.randomUUID()) to listOf("Project 1", "Project 2")
            val userId = UUID.randomUUID()
            val projectNumber = 1
            val newProjectName = "New Project Name"

            // when
            coEvery { session.currentSession.userId } returns userId
            coEvery { getAllProjectsUi() } returns projects
            coEvery { reader.readInput() } returns "invalid" andThen "1" andThen newProjectName
            coEvery {
                editProjectUseCase(
                    projects.first[projectNumber - 1],
                    newProjectName,
                    userId
                )
            } returns Unit

            editProjectUi.editProject()

            // Then
            verifySequence {
                viewer.logMessage("=== Edit Project ===")
                viewer.logMessage("=== View Project ===")
                viewer.log("Enter project Number: ")
                viewer.logError("Project Number should be a number")
                viewer.log("Enter project Number: ")
                viewer.log("Enter new project name: ")
            }
            coVerify {
                editProjectUseCase(
                    projects.first[projectNumber - 1],
                    newProjectName,
                    userId
                )
            }
        }

    @Test
    fun `should log error when new project name is null`() = runBlocking {
        // Given
        val userId = UUID.randomUUID()
        val projectNumber = 1
        val projects =
            listOf(UUID.randomUUID(), UUID.randomUUID()) to listOf("Project 1", "Project 2")
        val newProjectName = "New Project Name"


        // when
        coEvery { session.currentSession.userId } returns userId
        coEvery { getAllProjectsUi() } returns projects
        coEvery { reader.readInput() } returns projectNumber.toString() andThen null andThen newProjectName
        coEvery {
            editProjectUseCase(
                projects.first[projectNumber - 1],
                newProjectName,
                userId
            )
        } returns Unit

        // Act
        editProjectUi.editProject()

        // Then
        verifySequence {
            viewer.logMessage("=== Edit Project ===")
            viewer.logMessage("=== View Project ===")
            viewer.log("Enter project Number: ")
            viewer.log("Enter new project name: ")
            viewer.logError("Project name can't be empty")
            viewer.log("Enter new project name: ")
        }
        coVerify { editProjectUseCase(projects.first[projectNumber - 1], newProjectName, userId) }
    }
}
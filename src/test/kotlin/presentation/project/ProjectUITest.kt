package presentation.project

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.project.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

class ProjectUiTest {

    private val createProjectUi: CreateProjectUi = mockk(relaxed = true)
    private val deleteProjectUi: DeleteProjectUi = mockk(relaxed = true)
    private val editProjectUi: EditProjectUi = mockk(relaxed = true)
    private val getAllProjectsUi: GetAllProjectsUi = mockk(relaxed = true)
    private var viewer: Viewer = mockk(relaxed = true)
    private var reader: Reader = mockk()

    private lateinit var projectUi: ProjectUi

    @BeforeEach
    fun setUp() {
        // Initialize the real ProjectUi with mocked dependencies
        projectUi = ProjectUi(
            createProjectUi,
            deleteProjectUi,
            editProjectUi,
            getAllProjectsUi,
            viewer,
            reader
        )

        // Mock log messages
        every { viewer.logMessage(any()) } just Runs
        every { viewer.logError(any()) } just Runs
    }

    @Test
    fun `should display project management options and handle invalid input`() = runTest {
        every { reader.readInput() } returnsMany listOf("999", "0")  // Invalid input followed by exit

        projectUi()

        verifySequence {
            viewer.logMessage("=== Project UI ===")
            viewer.logMessage("1. Create Project")
            viewer.logMessage("2. Delete Project")
            viewer.logMessage("3. Edit Project")
            viewer.logMessage("4. View Project")
            viewer.logMessage("0. Back")
            viewer.logMessage("Enter your choice: ")
            reader.readInput()  // Reads 999
            viewer.logError("Invalid choice. Please try again.")
            // Loop restarts and processes exit
            viewer.logMessage("=== Project UI ===")
            viewer.logMessage("1. Create Project")
            viewer.logMessage("2. Delete Project")
            viewer.logMessage("3. Edit Project")
            viewer.logMessage("4. View Project")
            viewer.logMessage("0. Back")
            viewer.logMessage("Enter your choice: ")
            reader.readInput()  // Reads 0
        }
    }

    @Test
    fun `should call createProjectUi when option 1 is selected`() = runTest {
        every { reader.readInput() } returns "1" andThen "0"

        projectUi()

        coVerify { createProjectUi.createProject() }
    }

    @Test
    fun `should call deleteProjectUi when option 2 is selected`() = runTest {
        every { reader.readInput() } returns "2" andThen "0"

        projectUi()

        coVerify { deleteProjectUi.deleteProject() }
    }

    @Test
    fun `should call editProjectUi when option 3 is selected`() = runTest {
        every { reader.readInput() } returns "3" andThen "0"

        projectUi()

        coVerify { editProjectUi.editProject() }
    }

    @Test
    fun `should call getAllProjectsUi when option 4 is selected`() = runTest {
        val projects = listOf(UUID.randomUUID(), UUID.randomUUID()) to listOf("Project 1", "Project 2")

        every { reader.readInput() } returns "4" andThen "1" andThen "0"
        coEvery { getAllProjectsUi() } returns projects

        projectUi()

        coVerify { getAllProjectsUi() }
    }

    @Test
    fun `should call logError when invalid project ID is entered in option 4`() = runTest {
        val projects = listOf(UUID.randomUUID(), UUID.randomUUID()) to listOf("Project 1", "Project 2")

        every { reader.readInput() } returns "4" andThen "invalid" andThen "0"
        coEvery { getAllProjectsUi() } returns projects

        projectUi()

        verify { viewer.logError("Project Id should be a number") }
    }

    @Test
    fun `should return null when option 0 is selected`() = runTest {
        every { reader.readInput() } returns "0"

        val result = projectUi()

        assertEquals(null, result)
    }
}

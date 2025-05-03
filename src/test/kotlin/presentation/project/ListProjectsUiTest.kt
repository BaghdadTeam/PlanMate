package presentation.project

import logic.usecase.project.FakeProjectRepository
import logic.usecase.project.FakeTaskRepository
import org.baghdad.logic.model.entities.*
import org.baghdad.logic.usecase.project.ListProjectsUseCase
import org.baghdad.presentation.project.ProjectUi

import org.junit.Test
import java.util.*

class ListProjectsUiTest {

    @Test
    fun `should print all projects in a table`() {
        val fakeRepo = FakeProjectRepository()
        val useCase = ListProjectsUseCase(fakeRepo)

        val outputs = mutableListOf<String>()
        val ui = ProjectUi(
            useCase,
            editProjectUseCase = TODO(),
            deleteProjectUseCase = TODO(),
            listProjectsUseCase = TODO(),
        )

        // Create some projects
        val project1 = ProjectEntity(UUID.randomUUID(), "Alpha", "admin1")
        val project2 = ProjectEntity(UUID.randomUUID(), "Beta", "admin2")
        fakeRepo.createProject(project1)
        fakeRepo.createProject(project2)

        ui.listProjects()

        assert(outputs.any { it.contains("Alpha") })
        assert(outputs.any { it.contains("Beta") })
    }
}

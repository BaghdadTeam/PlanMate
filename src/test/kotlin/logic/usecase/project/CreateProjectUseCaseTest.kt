package logic.usecase.project

import junit.framework.TestCase.assertEquals
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.usecase.project.CreateProjectUseCase
import java.util.UUID
import kotlin.test.Test
import io.mockk.*
import org.baghdad.logic.repositories.ProjectRepository
import org.baghdad.logic.usecase.common.Result
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CreateProjectUseCaseTest {

    @Test
    fun `create project with unique name`() {
        // Arrange
        val repo = mockk<ProjectRepository>(relaxed = true)
        val useCase = CreateProjectUseCase(repo)
        val projectId = UUID.randomUUID()
        val admin = UserEntity(projectId, "admin", "pass", "", UserType.Admin)

        // Act
        val result = useCase("My Project", admin)

        // Assert
        assertTrue(result is Result.Success)

        // Verify that createProject was called once with a project matching the name and creator
        verify(exactly = 1) {
            repo.createProject(withArg {
                assertEquals("My Project", it.name)
                assertEquals(admin.id.toString(), it.creatorId)
            })
        }

        confirmVerified(repo)
    }
}

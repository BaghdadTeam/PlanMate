package logic.usecase.project

import junit.framework.TestCase.assertEquals
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.usecase.project.CreateProjectUseCase
import java.util.UUID
import kotlin.test.Test

class CreateProjectUseCaseTest {
    @Test
    fun `create project with unique name`() {
        val repo = FakeProjectRepository()
        val useCase = CreateProjectUseCase(repo)
        val admin = UserEntity(UUID.randomUUID(), "admin", "pass", "", UserType.Admin)
        useCase("My Project", admin)

        val all = repo.getAllProjects()
        assertEquals(1, all.size)
        assertEquals("My Project", all[0].name)
    }
}
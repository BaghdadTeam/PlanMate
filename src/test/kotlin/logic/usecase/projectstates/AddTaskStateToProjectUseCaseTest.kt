package logic.usecase.projectstates

import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import helpers.authentication.createUserHelper
import helpers.projectStates.ProjectStatesEntityTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.AccessDeniedException
import org.baghdad.logic.model.exceptions.CantAddStateWithNoNameException
import org.baghdad.logic.model.exceptions.StateNotAccessedException
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.usecase.admin.AdminPermissionCheckerUseCase
import org.baghdad.logic.usecase.projectstates.AddTaskStateToProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.*

class AddTaskStateToProjectUseCaseTest {

    private lateinit var statesRepository: ProjectStatesRepository
    private lateinit var auditRepository: AuditRepository
    private lateinit var createStateUseCase: AddTaskStateToProjectUseCase
    private val sessionManager: SessionManager = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        statesRepository = mockk(relaxed = true)
        auditRepository = mockk(relaxed = true)
        createStateUseCase = AddTaskStateToProjectUseCase(statesRepository, auditRepository ,sessionManager)
        coEvery { sessionManager.isAuthenticated() } returns true
    }
    @Test
    fun `should throw Unauthorized exception  when user not authenticated `() = runTest {
        coEvery { sessionManager.isAuthenticated() } returns false
        assertThrows<UnauthorizedException> {
            createStateUseCase.invoke(ProjectStatesEntityTestData.todoState(), UUID.randomUUID())
        }
    }

    @Test
    fun `should create state and add audit when state is valid`()= runTest {
        // given
        val userId = UUID.randomUUID()
        val state = ProjectStatesEntityTestData.todoState()

        // when
        createStateUseCase.invoke(state, userId)

        // then
        coVerify { statesRepository.createState(state) }

        val auditSlot = slot<AuditLogEntity>()
        coVerify { auditRepository.addAuditEntry(capture(auditSlot)) }

        val audit = auditSlot.captured
        assertThat(audit.projectId).isInstanceOf(UUID::class.java)
        assertThat(audit.timestamp).isInstanceOf(LocalDateTime::class.java)
    }


    @Test
    fun `should throw exception when state name is empty`()= runTest {
        // given
        val userId = UUID.randomUUID()
        val state = ProjectStatesEntityTestData.todoState().copy(name = "")

        // When & Then
        assertThrows<CantAddStateWithNoNameException> {
            createStateUseCase.invoke(state, userId)
        }
    }
}
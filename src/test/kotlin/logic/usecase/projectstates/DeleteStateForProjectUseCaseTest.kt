package logic.usecase.projectstates

import com.google.common.truth.Truth
import helpers.projectStates.ProjectStatesEntityTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.NotAccessException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.usecase.projectstates.DeleteStateForProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.*

class DeleteStateForProjectUseCaseTest {

    private lateinit var statesRepository: ProjectStatesRepository
    private lateinit var auditRepository: AuditRepository
    private lateinit var deleteStateUseCase: DeleteStateForProjectUseCase
    private lateinit var userRepository: UserRepository

    private val adminUser = UserEntity(
        name = "Narges Nagy",
        username = "nargesnagy",
        hashedPassword = "jkjhjkljlk",
        type = UserType.Admin
    )

    private val mateUser = UserEntity(
        name = "Narges Nagy",
        username = "narges21",
        hashedPassword = "tryhghjg",
        type = UserType.Mate
    )

    @BeforeEach
    fun setup() {
        statesRepository = mockk(relaxed = true)
        auditRepository = mockk(relaxed = true)
        userRepository = mockk(relaxed = true)
        deleteStateUseCase = DeleteStateForProjectUseCase(statesRepository, auditRepository, userRepository)
    }

    @Test
    fun `should delete state and add audit when state is valid`() = runTest{
        // given
        val state = ProjectStatesEntityTestData.todoState()
        val id = state.id
        // when
        deleteStateUseCase.invoke(id, adminUser.id)

        // then
        coVerify { statesRepository.deleteState(id) }

        val auditSlot = slot<AuditLogEntity>()
        coVerify { auditRepository.addAuditEntry(capture(auditSlot)) }

        val audit = auditSlot.captured
        Truth.assertThat(audit.entityId).isInstanceOf(UUID::class.java)
        Truth.assertThat(audit.timestamp).isInstanceOf(LocalDateTime::class.java)
    }


    @Test
    fun `should throw exception when user type is mate`() = runTest {
        // Given
        val stateId = UUID.randomUUID()
        coEvery { userRepository.getUserById(mateUser.id) } returns mateUser

        // When
        val exception = assertThrows<NotAccessException> {
            deleteStateUseCase.invoke(stateId, mateUser.id)
        }

        // Then
        Truth.assertThat(exception.message).contains("Only Admin can delete states")
    }
}
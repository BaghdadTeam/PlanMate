package logic.usecase.projectstates

import com.google.common.truth.Truth
import helpers.projectStates.ProjectStatesEntityTestData
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.baghdad.logic.model.entities.AuditEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.usecase.projectstates.DeleteStateForProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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
    fun `should delete state and add audit when state is valid`() {
        // given
        val state = ProjectStatesEntityTestData.todoState()
        val id = state.id
        // when
        deleteStateUseCase.invoke(id.toString(), adminUser.id)

        // then
        verify { statesRepository.deleteState(id.toString()) }

        val auditSlot = slot<AuditEntity>()
        verify { auditRepository.addAuditEntry(capture(auditSlot)) }

        val audit = auditSlot.captured
        Truth.assertThat(audit.entityId).isNotEmpty()
        Truth.assertThat(audit.timestamp).isNotEmpty()
    }

    @Test
    fun `should throw exception when state name is empty`() {
        // given
        val state = ProjectStatesEntityTestData.todoState().copy(name = "")
        // Then
        val exception = org.junit.jupiter.api.assertThrows<Exception> {
            deleteStateUseCase.invoke("", adminUser.id)
        }
        Truth.assertThat(exception.message).contains("Current state not found")
    }

}
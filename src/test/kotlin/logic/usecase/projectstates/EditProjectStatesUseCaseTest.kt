package logic.usecase.projectstates

import com.google.common.truth.Truth
import helpers.projectStates.ProjectStatesEntityTestData
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.usecase.projectstates.EditProjectStatesUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class EditProjectStatesUseCaseTest {

    private lateinit var statesRepository: ProjectStatesRepository
    private lateinit var auditRepository: AuditRepository
    private lateinit var editStateUseCase: EditProjectStatesUseCase
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
        editStateUseCase = EditProjectStatesUseCase(statesRepository, auditRepository, userRepository)
    }

    @Test
    fun `should delete state and add audit when state is valid`() = runTest {
        // given
        val state = ProjectStatesEntityTestData.todoState()
        val id = state.id
        val name = "to dooo"
        val newState = state.copy(id = id, name = name)
        // when
        editStateUseCase.invoke(id, newState, adminUser.id)

        // then
        coVerify { statesRepository.editState(id, newState) }

        val auditSlot = slot<AuditLogEntity>()
        coVerify { auditRepository.addAuditEntry(capture(auditSlot)) }

        val audit = auditSlot.captured
        Truth.assertThat(audit.entityId).isInstanceOf(UUID::class.java)
        Truth.assertThat(audit.timestamp).isInstanceOf(LocalDateTime::class.java)

    }


}

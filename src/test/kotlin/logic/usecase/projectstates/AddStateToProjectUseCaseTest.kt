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
import org.baghdad.logic.usecase.projectstates.AddStateToProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AddStateToProjectUseCaseTest {

    private lateinit var statesRepository: ProjectStatesRepository
    private lateinit var auditRepository: AuditRepository
    private lateinit var createStateUseCase: AddStateToProjectUseCase
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
        createStateUseCase = AddStateToProjectUseCase(statesRepository, auditRepository, userRepository)
    }


    @Test
    fun `should create state and add audit when state is valid`() {
        // given
        val state = ProjectStatesEntityTestData.todoState()

        // when
        createStateUseCase.invoke(state, adminUser.id)

        // then
        verify { statesRepository.createState(state) }

        val auditSlot = slot<AuditEntity>()
        verify { auditRepository.addAuditEntry(capture(auditSlot)) }

        val audit = auditSlot.captured
        Truth.assertThat(audit.entityId).isNotEmpty()
        Truth.assertThat(audit.timestamp).isNotEmpty()
    }

    @Test
    fun `should throw exception when user type is not admin`() {
        // given
        val state = ProjectStatesEntityTestData.todoState()

        // Then
        val exception = assertThrows<Exception> {
            createStateUseCase.invoke(state, mateUser.id)
        }
        Truth.assertThat(exception.message).contains("Only Admin can add tasks")
    }

    @Test
    fun `should throw exception when state name is empty`() {
        // given
        val state = ProjectStatesEntityTestData.todoState().copy(name = "")
        // Then
        val exception = assertThrows<Exception> {
            createStateUseCase.invoke(state, adminUser.id)
        }
        Truth.assertThat(exception.message).contains("state name can't be empty")
    }

}
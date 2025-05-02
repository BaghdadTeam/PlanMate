package logic.usecase.audit

import io.mockk.mockk
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.usecase.audit.GetAuditByTaskIdUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class GetAuditByTaskUseCaseTest {
    lateinit var  auditRepository : AuditRepository
    lateinit var  getAuditByTaskUseCase: GetAuditByTaskIdUseCase

    @BeforeEach
    fun setup(){
        auditRepository = mockk(relaxed = true)
        getAuditByTaskUseCase = GetAuditByTaskIdUseCase(auditRepository)

    }

    @Test
    fun `Should throw no exception when get audit by task id`(){
        // Given
        val ProjectUUID = UUID.randomUUID()
        // when & then
        getAuditByTaskUseCase.invoke(ProjectUUID)

    }
}
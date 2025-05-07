package logic.usecase.audit

import io.mockk.mockk
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.usecase.audit.GetAuditByProjectIdUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class GetAuditByProjectIdUseCaseTest {
    lateinit var  auditRepository : AuditRepository
    lateinit var  getAuditByProjectIdUseCase : GetAuditByProjectIdUseCase

    @BeforeEach
    fun setup(){
        auditRepository = mockk(relaxed = true)
        getAuditByProjectIdUseCase = GetAuditByProjectIdUseCase(auditRepository)

    }

    @Test
    fun `Should throw no exception when get audit by project id`(){
        // Given
        val ProjectUUID = UUID.randomUUID()
        // when & then
        getAuditByProjectIdUseCase.invoke(ProjectUUID)

    }
}
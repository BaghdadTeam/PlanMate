package data.repository.audit

import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import org.baghdad.data.repository.audit.AuditRepositoryImpl
import org.baghdad.logic.entities.AuditEntity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class AuditRepositoryImplTest {
    private lateinit var audit: AuditRepositoryImpl

    @BeforeEach
    fun setup() {
        audit = AuditRepositoryImpl()
    }

    @Test
    fun `Testing the jacoco test coverage`() {
        val result = audit.addAuditEntry(mockk())

        assertThat(result).isEqualTo(true)
    }
}
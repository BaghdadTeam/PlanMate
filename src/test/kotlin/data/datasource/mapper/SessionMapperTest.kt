package data.datasource.mapper

import com.google.common.truth.Truth
import helpers.authentication.SessionTestData
import org.baghdad.data.datasource.mapper.sesssion.SessionMapper
import org.baghdad.logic.model.entities.SessionEntity
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class SessionMapperTest {
    lateinit var sessionParser: SessionMapper

    @BeforeEach
    fun setUp() {
        sessionParser = SessionMapper()
    }

    @Test
    fun `Should parse session into Csv file`() {

        // Given
        val sessionData = SessionTestData.TestSession
        val session = SessionEntity(id = sessionData.id, userId = sessionData.userId, token = sessionData.token, loginTime = LocalDateTime.now())
        val expectedCsv = SessionTestData.TestSession.line
        // When
        val result = sessionParser.serializer(session)
        // Then
        Truth.assertThat(result).isEqualTo(expectedCsv)
    }

    @Test
    fun `Should deserialize session form csv file into SessionEntity`() {
        // Given
        val sessionData= SessionTestData.TestSession
        val line = SessionTestData.TestSession.line
        // When
        val result = sessionParser.deserializer(line)
        // Then
        Truth.assertThat(result.id).isEqualTo(sessionData.id)
        Truth.assertThat(result.token).isEqualTo(sessionData.token)
        Truth.assertThat(result.loginTime).isEqualTo(sessionData.loginTime)
        Truth.assertThat(result.userId).isEqualTo(sessionData.userId)
    }
}
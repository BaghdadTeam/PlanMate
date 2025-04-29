package data.datasource.parser.session

import com.google.common.truth.Truth.assertThat
import helpers.authentication.SessionTestData
import org.baghdad.data.datasource.parser.session.SessionParser
import org.baghdad.logic.entities.authentication.SessionEntity
import org.junit.jupiter.api.*
import java.time.LocalDateTime


class SessionParserTest {
    lateinit var sessionParser: SessionParser

    @BeforeEach
    fun setUp() {
        sessionParser = SessionParser()
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
        assertThat(result).isEqualTo(expectedCsv)
    }

    @Test
    fun `Should deserialize session form csv file into SessionEntity`() {
        // Given
        val sessionData= SessionTestData.TestSession
        val line = SessionTestData.TestSession.line
        // When
        val result = sessionParser.deserializer(line)
        // Then
        assertThat(result.id).isEqualTo(sessionData.id)
        assertThat(result.token).isEqualTo(sessionData.token)
        assertThat(result.loginTime).isEqualTo(sessionData.loginTime)
        assertThat(result.userId).isEqualTo(sessionData.userId)
    }
}
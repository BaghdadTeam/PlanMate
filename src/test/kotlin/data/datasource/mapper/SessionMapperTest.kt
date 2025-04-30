package data.datasource.mapper


import com.google.common.truth.Truth.assertThat
import helpers.authentication.SessionTestData
import org.baghdad.data.datasource.mapper.sesssion.SessionMapper
import org.baghdad.logic.model.entities.SessionEntity
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SessionMapperTest {
    lateinit var sessionMapper: SessionMapper

    @BeforeEach
    fun setUp() {
        sessionMapper = SessionMapper()
    }

    @Test
    fun `Should parse session into Csv file`() {

        // Given
        val sessionData = SessionTestData.TestSession
        val session = SessionEntity(id = sessionData.id, userId = sessionData.userId, token = sessionData.token, loginTime = sessionData.loginTime)
        val expectedCsv = SessionTestData.TestSession.line
        // When
        val result = sessionMapper.serializer(session)
        // Then
        assertThat(result).isEqualTo(expectedCsv)
    }
    @Test
    fun `Should parse header into csv file`() {
        val header =  "id,userId,token,loginTime"
        assertThat(sessionMapper.header()).isEqualTo(header)
    }

    @Test
    fun `Should deserialize session form csv file into SessionEntity`() {
        // Given
        val sessionData= SessionTestData.TestSession
        val line = SessionTestData.TestSession.line
        // When
        val result = sessionMapper.deserializer(line)
        // Then
        assertThat(result.id).isEqualTo(sessionData.id)
        assertThat(result.token).isEqualTo(sessionData.token)
        assertThat(result.loginTime).isEqualTo(sessionData.loginTime)
        assertThat(result.userId).isEqualTo(sessionData.userId)
    }
    @Test
    fun `deserializer throws IndexOutOfBoundsException for malformed line`() {
        // Only two fields instead of four
        val malformed = "123e4567-e89b-12d3-a456-426614174000,OnlyName"
        assertThrows<IndexOutOfBoundsException> { sessionMapper.deserializer(malformed) }
    }
}
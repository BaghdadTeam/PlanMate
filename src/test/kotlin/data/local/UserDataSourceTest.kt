package data.local

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.data.datasource.DataSource
import org.baghdad.data.dto.UserDto
import org.baghdad.data.local.UserDataSource
import org.baghdad.logic.model.enums.UserType
import org.baghdad.logic.model.exceptions.UserNotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class UserDataSourceTest {

    private lateinit var dataSource: DataSource<UserDto>
    private lateinit var userDataSource: UserDataSource

    private val sampleUser = UserDto(
        id = UUID.randomUUID(),
        name = "name",
        username = "username",
        hashedPassword = "hashedPassword",
        type = UserType.Admin.toString(),
    )

    @BeforeEach
    fun setup() {
        dataSource = mockk(relaxed = true)
        userDataSource = UserDataSource(dataSource)
    }

    @Test
    fun `loadUsers delegates to data source`() = runTest {
        // Given
        val list = listOf(sampleUser)
        coEvery { dataSource.loadAll() } returns list

        // When
        val result = userDataSource.loadUsers()

        // Then
        assertThat(result).isEqualTo(list)
        coEvery { dataSource.loadAll() }
    }

    @Test
    fun `findUserByUsername returns existing user`() = runTest {
        // Given
        coEvery { dataSource.loadAll() } returns listOf(sampleUser)

        // When
        val user = userDataSource.findUserByUsername(sampleUser.username)

        // Then
        assertThat(user).isEqualTo(sampleUser)
    }

    @Test
    fun `findUserByUsername throws when not found`() = runTest {
        // Given
        coEvery { dataSource.loadAll() } returns emptyList()

        // When & Then
        val error = assertThrows<UserNotFoundException> {
            userDataSource.findUserByUsername("missing")
        }
        assertThat(error.message).isEqualTo("User not found with username: missing")
    }

    @Test
    fun `findUserById returns existing user`() = runTest {
        // Given
        coEvery { dataSource.loadAll() } returns listOf(sampleUser)

        // When
        val user = userDataSource.findUserById(sampleUser.id)

        // Then
        assertThat(user).isEqualTo(sampleUser)
    }

    @Test
    fun `findUserById returns null when not found`() = runTest {
        // Given
        coEvery { dataSource.loadAll() } returns listOf(sampleUser)

        // When
        val user = userDataSource.findUserById(UUID.randomUUID())

        // Then
        assertThat(user).isNull()
    }


    @Test
    fun `should return true when username is taken`() = runTest {
        // Given
        coEvery { dataSource.loadAll() } returns listOf(sampleUser)

        // When
        val user = userDataSource.isUsernameTaken(sampleUser.username)

        // Then
        assertThat(user).isTrue()
    }

    @Test
    fun `should return false when username is not taken`() = runTest {
        // Given
        coEvery { dataSource.loadAll() } returns listOf()

        // When
        val user = userDataSource.isUsernameTaken("Aboud")

        // Then
        assertThat(user).isFalse()
    }
}

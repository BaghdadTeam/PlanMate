package org.baghdad.data.local

import com.google.common.truth.Truth.assertThat
import helpers.authentication.createUserHelper
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.baghdad.data.datasource.DataSource
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.exceptions.user.UserNotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class UserDataSourceTest {

    private lateinit var dataSource: DataSource<UserEntity>
    private lateinit var userDataSource: UserDataSource

    private val sampleUser = createUserHelper()

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
    fun `addUser delegates to data source append`() = runTest {
        // Given
        coEvery { dataSource.append(sampleUser) } just runs

        // When
        userDataSource.addUser(sampleUser)

        // Then
        coEvery { dataSource.append(sampleUser) }
    }

    @Test
    fun `findUserByUsername returns existing user`() = runTest {
        // Given
        coEvery { dataSource.loadAll() } returns listOf(sampleUser)

        // When
        val user = userDataSource.findUserByUsername("testuser")

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
}

package data.repositories.user

import com.google.common.truth.Truth.assertThat
import helpers.authentication.createUserHelper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.baghdad.data.local.UserDataSource
import org.baghdad.data.repositories.user.UserRepositoryImpl
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.exceptions.user.UserNotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class UserRepositoryImplTest {

    private lateinit var dataSource: UserDataSource
    private lateinit var repository: UserRepositoryImpl
    private lateinit var sampleUser: UserEntity

    @BeforeEach
    fun setup() {
        dataSource = mockk()
        repository = UserRepositoryImpl(dataSource)
        sampleUser = createUserHelper()
    }

    @Test
    fun `createUser delegates to dataSource`() = runTest {
        // Given
        coEvery { dataSource.addUser(sampleUser) } just runs
        // When
        repository.createUser(sampleUser)
        // Then
        coVerify { dataSource.addUser(sampleUser) }
    }

    @Test
    fun `findByUsername returns data source result`() = runTest {
        // Given
        coEvery { dataSource.findUserByUsername("sample") } returns sampleUser
        // When
        val result = repository.getUserByUsername("sample")
        // Then
        assertThat(result).isEqualTo(sampleUser)
        coVerify { dataSource.findUserByUsername("sample") }
    }

    @Test
    fun `getUserById returns existing user`() = runTest {
        // Given
        coEvery { dataSource.findUserById(sampleUser.id) } returns sampleUser
        // When
        val result = repository.getUserById(sampleUser.id)
        // Then
        assertThat(result).isEqualTo(sampleUser)
        coVerify { dataSource.findUserById(sampleUser.id) }
    }

    @Test
    fun `getUserById throws when user not found`() = runTest {
        // Given
        val missingId = UUID.randomUUID()
        coEvery { dataSource.findUserById(missingId) } returns null
        // When & Then
        val error = assertThrows<UserNotFoundException> {
            repository.getUserById(missingId)
        }
        assertThat(error.message).isEqualTo("User not found with id: $missingId")
        coVerify { dataSource.findUserById(missingId) }
    }

    @Test
    fun `getAllUsers returns data source list`() = runTest {
        // Given
        val list = listOf(sampleUser)
        coEvery { dataSource.loadUsers() } returns list
        // When
        val result = repository.getAllUsers()
        // Then
        assertThat(result).isEqualTo(list)
        coVerify { dataSource.loadUsers() }
    }

    @Test
    fun `should return true when username is taken`() {
        // Given
        coEvery { dataSource.isUsernameTaken(sampleUser.username) } returns true
        // When
        val result = runBlocking { repository.isUsernameTaken(sampleUser.username) }
        // Then
        assertThat(result).isTrue()
        coVerify { dataSource.isUsernameTaken(sampleUser.username) }
    }

}

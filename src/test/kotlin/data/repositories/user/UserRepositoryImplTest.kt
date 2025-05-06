package org.baghdad.data.repositories.user

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.baghdad.data.local.UserDataSource
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
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
        sampleUser = UserEntity(
            id             = UUID.randomUUID(),
            name           = "Sample User",
            username       = "sample",
            hashedPassword = "hash",
            type           = UserType.Mate
        )
    }

    @Test
    fun `createUser delegates to dataSource`() {
        // Given
        every { dataSource.addUser(sampleUser) } just runs
        // When
        repository.createUser(sampleUser)
        // Then
        verify { dataSource.addUser(sampleUser) }
    }

    @Test
    fun `findByUsername returns data source result`() {
        // Given
        every { dataSource.findUserByUsername("sample") } returns sampleUser
        // When
        val result = repository.findByUsername("sample")
        // Then
        assertThat(result).isEqualTo(sampleUser)
        verify { dataSource.findUserByUsername("sample") }
    }

    @Test
    fun `getUserById returns existing user`() {
        // Given
        every { dataSource.findUserById(sampleUser.id) } returns sampleUser
        // When
        val result = repository.getUserById(sampleUser.id)
        // Then
        assertThat(result).isEqualTo(sampleUser)
        verify { dataSource.findUserById(sampleUser.id) }
    }

    @Test
    fun `getUserById throws when user not found`() {
        // Given
        val missingId = UUID.randomUUID()
        every { dataSource.findUserById(missingId) } returns null
        // When & Then
        val error = assertThrows<UserNotFoundException> {
            repository.getUserById(missingId)
        }
        assertThat(error.message).isEqualTo("User not found with id: $missingId")
        verify { dataSource.findUserById(missingId) }
    }

    @Test
    fun `getAllUsers returns data source list`() {
        // Given
        val list = listOf(sampleUser)
        every { dataSource.loadUsers() } returns list
        // When
        val result = repository.getAllUsers()
        // Then
        assertThat(result).isEqualTo(list)
        verify { dataSource.loadUsers() }
    }
}

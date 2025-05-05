package data.repositories.user

import org.baghdad.logic.model.entities.UserType

import io.mockk.*
import org.baghdad.data.local.UserDataSource
import org.baghdad.data.repositories.user.UserRepositoryImpl
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.exceptions.user.UserNotFoundException
import org.baghdad.logic.repositories.UserRepository

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.stopKoin
import org.koin.dsl.module

import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UserRepositoryImplTest {

    private val dataSource: UserDataSource = mockk(relaxed = true)
    private lateinit var repository: UserRepository

    private val testUser = UserEntity(
        id = UUID.randomUUID(),
        username = "testuser",
        name = "Test User",
        hashedPassword = "hashedpass",
        type = UserType.Admin
    )

    @Before
    fun setup() {
        val testModule = module {
            single<UserDataSource> { dataSource }
            single<UserRepository> { UserRepositoryImpl(get()) }
        }

        org.koin.core.context.startKoin {
            modules(testModule)
        }

        repository = UserRepositoryImpl(dataSource)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `createUser should call addUser on dataSource`() {
        repository.createUser(testUser)
        verify { dataSource.addUser(testUser) }
    }

    @Test
    fun `findByUsername should return correct user`() {
        every { dataSource.findUserByUsername("testuser") } returns testUser

        val result = repository.findByUsername("testuser")
        assertEquals(testUser, result)
        verify { dataSource.findUserByUsername("testuser") }
    }

    @Test
    fun `getUserById should return user if found`() {
        every { dataSource.findUserById(testUser.id) } returns testUser

        val result = repository.getUserById(testUser.id)
        assertEquals(testUser, result)
        verify { dataSource.findUserById(testUser.id) }
    }

    @Test
    fun `getUserById should throw exception if user not found`() {
        val userId = UUID.randomUUID()
        every { dataSource.findUserById(userId) } returns null

        assertFailsWith<UserNotFoundException> {
            repository.getUserById(userId)
        }

        verify { dataSource.findUserById(userId) }
    }

    @Test
    fun `getAllUsers should return list of users`() {
        val userList = listOf(testUser)
        every { dataSource.loadUsers() } returns userList

        val result = repository.getAllUsers()
        assertEquals(userList, result)
        verify { dataSource.loadUsers() }
    }
}

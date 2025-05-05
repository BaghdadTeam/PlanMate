package org.baghdad.data.local

import org.baghdad.data.datasource.DataSource
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.user.UserNotFoundException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class UserDataSourceTest {

    private lateinit var fakeDataSource: FakeUserDataSource
    private lateinit var userDataSource: UserDataSource
    private lateinit var sampleUser: UserEntity

    @BeforeEach
    fun setup() {
        fakeDataSource = FakeUserDataSource()
        userDataSource = UserDataSource(fakeDataSource)

        val sampleUser = UserEntity(
            id = UUID.randomUUID(),
            username = "testuser",
            name = "Test User",
            hashedPassword = "hashed-pass123",
            type = UserType.Admin
        )


        fakeDataSource.append(sampleUser)
    }

    @Test
    fun `loadUsers returns all users`() {
        val users = userDataSource.loadUsers()
        assertEquals(1, users.size)
        assertEquals("testuser", users[0].username)
    }

    @Test
    fun `addUser adds user successfully`() {
        val newUser = sampleUser.copy(
            id = UUID.randomUUID(),
            username = "newuser"
        )
        userDataSource.addUser(newUser)

        val users = userDataSource.loadUsers()
        assertTrue(users.any { it.username == "newuser" })
    }

    @Test
    fun `findUserByUsername returns user if found`() {
        val user = userDataSource.findUserByUsername("testuser")
        assertEquals("Test User", user.name)
    }

    @Test
    fun `findUserByUsername throws exception if not found`() {
        val exception = assertThrows(UserNotFoundException::class.java) {
            userDataSource.findUserByUsername("unknown")
        }
        assertEquals("User not found with username: unknown", exception.message)
    }

    @Test
    fun `findUserById returns user if found`() {
        val user = userDataSource.findUserById(sampleUser.id)
        assertNotNull(user)
        assertEquals("testuser", user?.username)
    }

    @Test
    fun `findUserById returns null if not found`() {
        val user = userDataSource.findUserById(UUID.randomUUID())
        assertNull(user)
    }

    // Fake DataSource for testing
    class FakeUserDataSource : DataSource<UserEntity> {
        private val users = mutableListOf<UserEntity>()

        override fun loadAll(): List<UserEntity> = users

        override fun append(item: UserEntity) {
            users.add(item)
        }
        override fun update(entities: List<UserEntity>) {
            // يمكن تركها فارغة للتجربة
        }
    }
}

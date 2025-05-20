package org.baghdad.data.datasource.remote.mongodb.collection

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.toList
import org.baghdad.data.dto.UserDto
import org.baghdad.logic.model.exceptions.UserNotFoundException
import java.util.*

class UserCollection(
    private val collection: MongoCollection<UserDto>
) {

    suspend fun createUser(user: UserDto) {
        collection.insertOne(user)
    }

    suspend fun findUserById(id: UUID): UserDto {
        val filter = Filters.eq("_id", id)
        return collection.find(filter).toList().firstOrNull()
            ?: throw UserNotFoundException("User not found with id: $id")
    }

    suspend fun isUsernameTaken(username: String): Boolean {
        val filter = Filters.eq("username", username)
        val result = collection.find(filter).toList().firstOrNull()
        return result != null
    }

    suspend fun findUserByUsername(username: String): UserDto {
        val filter = Filters.eq("username", username)
        return collection.find(filter).toList().firstOrNull()
            ?: throw UserNotFoundException("User not found with username: $username")
    }

    suspend fun loadUsers(): List<UserDto> {
        return collection.find().toList()
    }
}
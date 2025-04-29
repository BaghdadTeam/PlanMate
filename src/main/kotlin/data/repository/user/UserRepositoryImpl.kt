package org.baghdad.data.repository.user

import org.baghdad.data.datasource.csv.CsvDataSourceImpl
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.repositories.UserRepository

class UserRepositoryImpl(
    private val csvDataSourceImpl: CsvDataSourceImpl<UserEntity>
) : UserRepository {
    override fun createUser(username: String, userCreator: UserEntity) {
        if (userCreator.type == UserType.Admin) {

        }
    }

    override fun getUserByUsername(username: String): UserEntity? {
        TODO("Not yet implemented")
    }

    override fun getAllUsers(): List<UserEntity> {

        TODO("Not yet implemented")
    }
}

/*


[0, 1, 2, 9, 4, 5, 3]

read =>
append =>
delete => userID, username


 */
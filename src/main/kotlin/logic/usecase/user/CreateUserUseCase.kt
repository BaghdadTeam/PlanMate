package org.baghdad.logic.usecase.user

import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.utils.md5WithSalt
import java.util.UUID


class CreateUserUseCase(
    private val userRepository: UserRepository,
    private val validatorUseCase: UserValidatorUseCase
) {
    suspend operator fun invoke(
        username: String,
        passwordPlain: String,
        name: String,
        creatorId: UUID
    ): UserEntity {
        validatorUseCase.invoke(username, passwordPlain, name, creatorId)
        val newUser = createUserEntity(username, passwordPlain, name)
        userRepository.createUser(newUser)
        return newUser
    }

    private fun createUserEntity(username: String, password: String, name: String): UserEntity {
        return UserEntity(
            name = name,
            username = username,
            hashedPassword = password.md5WithSalt(),
            type = UserType.Mate
        )
    }
}

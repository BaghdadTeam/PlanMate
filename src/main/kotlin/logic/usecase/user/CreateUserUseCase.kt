package org.baghdad.logic.usecase.user

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.utils.md5WithSalt
import java.util.UUID


class CreateUserUseCase(
    private val userRepository: UserRepository,
    private val validatorUseCase: UserValidatorUseCase,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(
        username: String,
        passwordPlain: String,
        name: String,
        creatorId: UUID
    ): UserEntity {
        if (!sessionManager.isAuthenticated())  throw UnauthorizedException(" Not authenticated")
        validatorUseCase.invoke(username, passwordPlain, name, creatorId)
        val newUser = createUserEntity(username,  name)
        userRepository.createUser(newUser,passwordPlain.md5WithSalt())
        return newUser
    }

    private fun createUserEntity(username: String, name: String): UserEntity {
        return UserEntity(
            name = name,
            username = username,
            type = UserType.Mate
        )
    }
}

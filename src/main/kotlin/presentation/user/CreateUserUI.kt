package org.baghdad.presentation.user

import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.usecase.user.CreateUserResult
import org.baghdad.logic.usecase.user.CreateUserUseCase
import org.baghdad.presentation.Console

class CreateUserUI(
    private val console: Console,
    private val createUser: CreateUserUseCase
) {
    fun run(currentUser: UserEntity?) {
        if (currentUser?.type != org.baghdad.logic.model.entities.UserType.Admin) {
            console.writeLine("🚫 Only administrators can create new users.")
            return
        }
        console.writeLine("=== Create New Mate ===")
        val username = console.readLine("Username: ").trim()
        val name     = console.readLine("Name:     ").trim()
        val pass     = console.readLine("Password: ").trim()

        when (val result = createUser(username, pass, name, currentUser)) {
            is CreateUserResult.Success -> console.writeLine("✅ User '${result.user.username}' created successfully.")
            is CreateUserResult.Failure -> console.writeLine("⚠️ Error: ${result.error}")
        }
    }
}

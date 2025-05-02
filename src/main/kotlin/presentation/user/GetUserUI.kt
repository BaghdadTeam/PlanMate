package org.baghdad.presentation.user

import org.baghdad.presentation.Console
import org.baghdad.logic.usecase.user.GetUserByUsernameUseCase
import org.baghdad.logic.usecase.user.GetUserResult

class GetUserUI(
    private val console: Console,
    private val getByUsername: GetUserByUsernameUseCase
) {
    fun run() {
        console.writeLine("=== Find User ===")
        val username = console.readLine("Username: ").trim()

        when (val result = getByUsername(username)) {
            is GetUserResult.Success -> {
                val u = result.user
                console.writeLine("✅ Found: ${u.username}  Name: ${u.name}  Role: ${u.type}")
            }
            GetUserResult.NotFound -> {
                console.writeLine("⚠️ User '$username' not found.")
            }
        }
    }
}

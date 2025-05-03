package org.baghdad.presentation.user

import org.baghdad.logic.usecase.user.GetUserByUsernameUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer

class GetUserUI(
    private val reader: Reader,  // تغيير من Console إلى Reader
    private val viewer: Viewer,  // تغيير من Console إلى Viewer
    private val getByUsername: GetUserByUsernameUseCase
) {
    fun run() {
        viewer.logMessage("=== Find User ===")
        val username = reader.readInput()?.trim() ?: ""
        val result = getByUsername(username)
        result
            .onSuccess { user ->
                viewer.logMessage("✅ Found: ${user.username}  Name: ${user.name}  Role: ${user.type}")
            }
            .onFailure {
                viewer.logMessage("⚠️ User '$username' not found.")
            }
    }
}

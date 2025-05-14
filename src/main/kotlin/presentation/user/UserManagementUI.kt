package org.baghdad.presentation.user

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer

class UserManagementUI(
    private val reader: Reader,
    private val viewer: Viewer,
    private val session: SessionManager,
    private val userRepository: UserRepository,
    private val createUserUI: CreateUserUI,
    private val getUserByUsernameUI: GetUserByUsernameUI,
) {
    suspend operator fun invoke() {
        val currentUserEntity = userRepository.getUserById(session.currentSession.userId)

        if (currentUserEntity.type != UserType.Admin) {
            viewer.logError("Unauthorized: Only admins can access user management.")
            return
        }

        while (true) {
            viewer.logMessage(
                """
                === User Management Menu ===
                1. Create New User
                2. Find User by Username
                0. Back to Main Menu
                """.trimIndent()
            )

            when (reader.readInput()?.trim()) {
                "1" -> createUserUI.invoke()
                "2" -> getUserByUsernameUI.run()
                "0" -> {
                    viewer.logMessage("Returning to main menu...")
                    break
                }

                else -> viewer.logError("Invalid choice. Please try again.")
            }
        }
    }
}

package org.baghdad.presentation

import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.user.CreateUserUI
import org.baghdad.presentation.user.GetUserUI

class PlanMateCLI(
    private val reader: Reader,
    private val viewer: Viewer,
    private val createUserUI: CreateUserUI,
    private val getUserUI: GetUserUI
) {

    private var loggedInUser: UserEntity? = null

    fun start() {
        loggedInUser = UserEntity(
            name = "System Admin",
            username = "admin",
            hashedPassword = "",
            type = UserType.Admin
        )
        while (true) {
            viewer.logMessage("1) Create Mate User")
            viewer.logMessage("2) Find User by Username")
            viewer.logMessage("3) Exit")

            val choice = reader.readInput()?.trim()
            when (choice) {
                "1" -> createUserUI.run(loggedInUser)
                "2" -> getUserUI.run()
                "3" -> {
                    viewer.logMessage("Goodbye!")
                    return
                }

                null, "" -> viewer.logMessage("Invalid choice.")
                else -> viewer.logMessage("Invalid choice.")
            }
        }

    }

}

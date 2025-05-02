package org.baghdad.presentation

// org.baghdad.presentation.PlanMateCLI.kt
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.presentation.user.CreateUserUI
import org.baghdad.presentation.user.GetUserUI
import org.koin.core.component.KoinComponent

class PlanMateCLI(
    private val console: Console,
    private val createUserUI: CreateUserUI,
    private val getUserUI: GetUserUI
) : KoinComponent {

    private var loggedInUser: UserEntity? = null

    fun start() {
        loggedInUser = UserEntity(
            name = "System Admin",
            username = "admin",
            hashedPassword = "",
            type = UserType.Admin
        )
        while (true) {
            console.writeLine("\n--- PlanMate CLI Menu ---")
            console.writeLine("1) Create Mate User")
            console.writeLine("2) Find User by Username")
            console.writeLine("3) Exit")
            when (console.readLine("Choice: ").trim()) {
                "1" -> createUserUI.run(loggedInUser)
                "2" -> getUserUI.run()
                "3" -> {
                    console.writeLine("Goodbye!")
                    return
                }

                else -> console.writeLine("Invalid choice.")
            }
        }
    }
}

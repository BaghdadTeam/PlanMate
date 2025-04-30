package org.baghdad

import org.baghdad.data.local.ProjectDataSource
import org.baghdad.di.appModule
import org.baghdad.di.useCaseModule
import org.baghdad.logic.model.entities.Entities
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.usecase.StateTransitionUseCase
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.mp.KoinPlatform.getKoin
import java.util.UUID

fun main() {
    startKoin {
        modules(appModule, useCaseModule)
    }

    val test = ProjectDataSource(
        getKoin().get(qualifier = named(Entities.Project.name))
    )
    test.createProject()

    // Simulate a logged-in user
    val user = UserEntity(
        UUID.randomUUID(), // replace with actual user ID from your data
        name = "John Doe",
        username = "johndoe",
        hashedPassword = "hashedPassword",
        type = UserType.Mate
    )

    val service = getKoin().get<StateTransitionUseCase>()

    val taskId =
        "existing-task-id"     // Replace with a real task UUID from your test DB or fake data
    val newStateId = "new-state-id"     // Replace with a real state UUID

    val result = service.changeTaskState(taskId, newStateId, user)

    if (result.isSuccess) {
        println("Task state changed successfully!")
    } else {
        println("Failed to change task state: ${result.exceptionOrNull()?.message}")
    }
}
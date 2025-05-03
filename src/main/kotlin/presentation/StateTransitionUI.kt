package org.baghdad.presentation

import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.StateExceptions.NotFoundException
import org.baghdad.logic.usecase.StateTransitionUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer

class StateTransitionUI(
    private val useCase: StateTransitionUseCase,
    private val viewer: Viewer,
    private val reader: Reader
) {
    fun execute() {

        viewer.logMessage("Enter Task ID:")
        val taskId = reader.readInput()?.trim()

        if (taskId.isNullOrBlank()) throw Exception("Task ID cannot be null or blank.")


        viewer.logMessage("Enter New State ID:")
        val newStateId = reader.readInput()?.trim()

        if (newStateId.isNullOrBlank()) throw Exception("New State ID cannot be null or blank.")

        // todo: replace with actual logged-in user
        val user = UserEntity(
            username = "nadeen",
            name = "nadeen",
            hashedPassword = "123456789eef",
            type = UserType.Mate
        )

        try {
            //todo
            useCase.changeTaskState(taskId, newStateId, user)
            viewer.logMessage("Task state changed successfully.")
        } catch (e: NotFoundException) {
            viewer.logError("State not found in this project: ${e.message}")
        } catch (e: IllegalStateException) {
            viewer.logError("Invalid operation: ${e.message}")
        } catch (e: RuntimeException) {
            viewer.logError("Unexpected error: ${e.message}")
        }
    }
}
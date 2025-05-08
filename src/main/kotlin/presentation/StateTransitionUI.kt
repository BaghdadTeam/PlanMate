package org.baghdad.presentation

import kotlinx.coroutines.runBlocking
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.exceptions.StateExceptions.NotFoundException
import org.baghdad.logic.usecase.StateTransitionUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import java.util.UUID

class StateTransitionUI(
    private val useCase: StateTransitionUseCase,
    private val viewer: Viewer,
    private val reader: Reader
) {
    fun execute(user: UserEntity) {
        viewer.logMessage("Enter Task ID:")
        val taskId = reader.readInput()?.trim()

        if (taskId.isNullOrBlank()) throw Exception("Task ID cannot be null or blank.")

        viewer.logMessage("Enter New State ID:")
        val newStateId = reader.readInput()?.trim()

        if (newStateId.isNullOrBlank()) throw Exception("New State ID cannot be null or blank.")

        try {
            runBlocking {
                useCase.changeTaskState(UUID.fromString(taskId), UUID.fromString(newStateId), user)
                viewer.logMessage("Task state changed successfully.")
            }
        } catch (e: NotFoundException) {
            viewer.logError("State not found in this project: ${e.message}")
        } catch (e: IllegalStateException) {
            viewer.logError("Invalid operation: ${e.message}")
        } catch (e: RuntimeException) {
            viewer.logError("Unexpected error: ${e.message}")
        } catch (e: Exception) {
            viewer.logError("Something went wrong while trying to change task state: ${e.message}")

        }
    }
}

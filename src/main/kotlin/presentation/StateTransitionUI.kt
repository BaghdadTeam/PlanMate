package org.baghdad.presentation

import kotlinx.coroutines.runBlocking
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.exceptions.StateExceptions.NotFoundException
import org.baghdad.logic.usecase.StateTransitionUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import java.util.*

class StateTransitionUI(
    private val useCase: StateTransitionUseCase,
    private val viewer: Viewer,
    private val reader: Reader,
    private val session: SessionManager,
) {
    fun execute(tasksStates: List<StateEntity>, tasks: List<TaskEntity>) {

        viewer.logMessage("== All Tasks == ")
        tasks.forEachIndexed { index, task ->
            viewer.logMessage("${index + 1}. ${task.title} - ${task.description}")
        }

        viewer.logMessage("Enter Task Number:")
        val taskId = reader.readInput()?.trim()

        if (taskId.isNullOrBlank()) throw Exception("Task ID cannot be null or blank.")

        viewer.logMessage("== All States == ")

        tasksStates.forEachIndexed { index, state ->
            viewer.logMessage("${index + 1}. ${state.name}")
        }
        viewer.logMessage("Enter New State Number: ")
        val newStateId = reader.readInput()?.trim()

        if (newStateId.isNullOrBlank()) throw Exception("New State ID cannot be null or blank.")

        val userId = session.currentSession.userId

        try {
            //todo
            runBlocking {
                useCase.changeTaskState(UUID.fromString(taskId), UUID.fromString(newStateId), userId)
                viewer.logMessage("Task state changed successfully.")
            }
        } catch (e: NotFoundException) {
            viewer.logError("State not found in this project: ${e.message}")
        } catch (e: IllegalStateException) {
            viewer.logError("Invalid operation: ${e.message}")
        } catch (e: RuntimeException) {
            viewer.logError("Unexpected error: ${e.message}")
        } catch (_: Exception) {
            viewer.logError(" something went wrong while trying to change task state.")
        }
    }
}
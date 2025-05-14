package org.baghdad.presentation.task

import kotlinx.coroutines.runBlocking
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.TaskStateEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.exceptions.NotFoundException
import org.baghdad.logic.usecase.task.TaskStateTransitionUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import java.util.UUID

class TaskStateTransitionUI(
    private val useCase: TaskStateTransitionUseCase,
    private val session: SessionManager,
    private val viewer: Viewer,
    private val reader: Reader,
) {
    fun execute(tasksStates: List<TaskStateEntity>, tasks: List<TaskEntity>) {

        val taskId = getTaskIdFromUser(tasks)

        val taskStateId = getTaskStateIdFromUser(tasksStates)

        val userId = session.currentSession.userId

        try {
            runBlocking {
                useCase.changeTaskState(taskId, taskStateId , userId)
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

    private fun getTaskIdFromUser(tasks: List<TaskEntity>): UUID {
        viewer.logMessage("== All Tasks == ")

        tasks.forEachIndexed { index, task ->
            viewer.logMessage("${index + 1}. ${task.title} - ${task.description}")
        }

        viewer.logMessage("Enter Task Number:")
        val userTaskNumberChoice = reader.readInput()?.trim()

        if (userTaskNumberChoice.isNullOrBlank())
            throw Exception("Task ID cannot be null or blank.")

        if (userTaskNumberChoice.toInt() > tasks.size)
            throw Exception("Invalid choice.")

        return tasks[userTaskNumberChoice.toInt() - 1].id
    }

    private fun getTaskStateIdFromUser(tasksStates: List<TaskStateEntity>): UUID {
        viewer.logMessage("== All States == ")

        tasksStates.forEachIndexed { index, state ->
            viewer.logMessage("${index + 1}. ${state.name}")
        }

        viewer.logMessage("Enter New State Number: ")
        val userStateNumberChoice = reader.readInput()?.trim()

        if (userStateNumberChoice.isNullOrBlank())
            throw Exception("New State ID cannot be null or blank.")

        if (userStateNumberChoice.toInt() > tasksStates.size)
            throw Exception("Invalid choice.")

        return tasksStates[userStateNumberChoice.toInt() - 1].id
    }
}
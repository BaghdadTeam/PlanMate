package org.baghdad.presentation.task

import kotlinx.coroutines.runBlocking
import org.baghdad.logic.model.exceptions.TasksNotFoundException
import org.baghdad.logic.usecase.task.GetTasksByProjectIdUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import java.util.UUID

class GetTasksByProjectIdUI(
    private val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase,
    private val viewer: Viewer,
    private val reader: Reader
) {

    fun execute(projectId: UUID): List<UUID> {
        return tryGetTasks(projectId)
    }

    private fun tryGetTasks(projectId: UUID): List<UUID> {
        return try {
            runBlocking {
                val tasks = getTasksByProjectIdUseCase(projectId)
                if (tasks.isEmpty()) {
                    viewer.logMessage("No tasks found for the given project.")
                    emptyList()
                } else {
                    viewer.logMessage("Tasks for project $projectId:")
                    tasks.forEachIndexed { index, task ->
                        viewer.logMessage("${index + 1}. ${task.title} - ${task.description}")
                    }
                    tasks.map { it.id }
                }
            }
        } catch (_: TasksNotFoundException) {
            viewer.logMessage("No tasks found for project ID: $projectId")
            emptyList()
        } catch (e: Exception) {
            viewer.logMessage("Failed to get tasks: ${e.message}")
            emptyList()
        }
    }
}


//package org.baghdad.presentation.task
//import kotlinx.coroutines.runBlocking
//import org.baghdad.logic.model.exceptions.TasksNotFoundException
//import org.baghdad.logic.usecase.task.GetTasksByProjectIdUseCase
//import org.baghdad.presentation.input.Reader
//import org.baghdad.presentation.output.Viewer
//import java.util.UUID
//
//class GetTasksByProjectIdUI(
//    private val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase,
//    private val viewer: Viewer,
//    private val reader: Reader
//) {
//
//    fun execute(projectId: UUID) {
//        tryGetTasks(projectId)
//    }
//
//    private fun promptForProjectId(): UUID? {
//        viewer.logMessage("Please enter the project ID to fetch tasks:")
//        val input = reader.readInput()
//
//        if (input.isNullOrBlank()) {
//            viewer.logMessage("Project ID cannot be empty.")
//            return null
//        }
//
//        return UUID.fromString(input)
//    }
//
//    private fun tryGetTasks(projectId: UUID) {
//        try {
//            runBlocking {
//                val tasks = getTasksByProjectIdUseCase(projectId)
//                if (tasks.isEmpty()) {
//                    viewer.logMessage("No tasks found for the given project.")
//                } else {
//                    viewer.logMessage("Tasks for project $projectId:")
//                    tasks.forEachIndexed { index, task ->
//                        viewer.logMessage("${index + 1}. ${task.title} - ${task.description}")
//                    }
//                }
//            }
//        } catch (_: TasksNotFoundException) {
//            viewer.logMessage("No tasks found for project ID: $projectId")
//        } catch (e: Exception) {
//            viewer.logMessage("Failed to get tasks: ${e.message}")
//        }
//    }
//}
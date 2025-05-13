package org.baghdad.presentation.project

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.usecase.project.EditProjectUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer

class EditProjectUi(
    private val editProjectUseCase: EditProjectUseCase,
    private val sessionManager: SessionManager,
    private val viewer: Viewer,
    private val getAllProjectsUi: GetAllProjectsUi,
    private val reader: Reader

) {
    suspend fun editProject() {
        val session = sessionManager.currentSession
        val userId = session.userId

        viewer.logMessage("=== Edit Project ===")
        viewer.logMessage("=== View Project ===")
        val ids = getAllProjectsUi()

        viewer.logMessage("Enter new project name: ")

        val projectId = reader.readInput()?.toIntOrNull()
        val newName = reader.readInput()
        if (projectId != null && newName != null) {
            editProjectUseCase(ids[projectId], newName, userId)
        } else {
            viewer.logError("Project Id should be a number")
        }

    }
}
package org.baghdad.presentation.project

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.usecase.project.DeleteProjectUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer

class DeleteProjectUi(
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val sessionManager: SessionManager,
    private val getAllProjectsUi: GetAllProjectsUi,
    private val viewer: Viewer,
    private val reader: Reader
) {
    suspend fun deleteProject() {

        val session = sessionManager.currentSession
        val userId = session.userId

        viewer.logMessage("=== Delete Project ===")
        viewer.logMessage("=== View Project ===")
        val ids = getAllProjectsUi()
        val projectsId = reader.readInput()?.toIntOrNull()

        if (projectsId != null) {
            deleteProjectUseCase(ids[projectsId], userId)
        } else {
            viewer.logError("Project Id should be a number")
        }
    }
}
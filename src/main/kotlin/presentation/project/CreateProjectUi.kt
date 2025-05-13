package org.baghdad.presentation.project

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.usecase.project.CreateProjectUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer

class CreateProjectUi(
    private val createProjectUseCase: CreateProjectUseCase,
    private val sessionManager: SessionManager,
    private val viewer: Viewer,
    private val reader: Reader
) {
    suspend fun createProject() {
        val userId = sessionManager.currentSession.userId
        viewer.logMessage("=== Create Project ===")
        viewer.logMessage("Enter project name: ")
        val name = reader.readInput()
        if (name != null) {
            createProjectUseCase(name, userId)
        } else {
            viewer.logError("Project Id should be a number")
        }
    }
}
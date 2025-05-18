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
        val userId = sessionManager.currentSession.userId

        viewer.logMessage("=== Edit Project ===")
        viewer.logMessage("=== View Project ===")
        val projects = getAllProjectsUi()

        viewer.logMessage("Enter project Number: ")
        val projectIndex = reader.readInput()?.toIntOrNull()

        viewer.logMessage("Enter new project name: ")
        val newName = reader.readInput()

        if (projectIndex != null && newName != null) {
            if((projectIndex > 0 && projectIndex <= projects.first.size)) {
                editProjectUseCase(projects.first[projectIndex - 1], newName, userId)
            }else{
                viewer.logMessage("Invalid project Number")
            }
        } else {
            viewer.logError("Project Number should be a number")
        }
    }
}
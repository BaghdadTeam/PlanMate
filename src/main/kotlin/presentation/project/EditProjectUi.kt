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

        val projectIndex = getProjectNumberFromUser()
        val newName = getProjectNewNameFromUser()

        if ((projectIndex > 0 && projectIndex <= projects.first.size)) {
            editProjectUseCase(projects.first[projectIndex - 1], newName, userId)
        } else {
            viewer.logMessage("Invalid project Number")
        }
    }


    private fun getProjectNewNameFromUser(): String {
        viewer.log("Enter new project name: ")
        val newName = reader.readInput()
        return if (!newName.isNullOrBlank()) {
            newName
        } else {
            getProjectNewNameFromUser()
        }
    }

    private fun getProjectNumberFromUser(): Int {
        viewer.log("Enter project Number: ")
        val projectNumber = reader.readInput()?.toIntOrNull()
        return if (projectNumber != null) {
            projectNumber
        } else {
            viewer.logError("Project Number should be a number")
            getProjectNumberFromUser()
        }

    }

}
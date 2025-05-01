package org.baghdad.logic.usecase.report
import org.baghdad.logic.model.entities.ProjectSummaryReport
import org.baghdad.logic.repositories.ProjectRepository
import org.baghdad.logic.repositories.StateRepository
import org.baghdad.logic.repositories.TaskRepository

class ReportService (
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository,
    private val stateRepository: StateRepository
) {
    fun summary(): List<ProjectSummaryReport> {
        val projects = projectRepository.getAllProjects()


        return projects.map { project ->
            val tasks = taskRepository.getTasksByProjectId(project.id.toString())
            val totalTasks = tasks.size
            val tasksPerState = tasks.groupingBy { task -> task.stateId }.eachCount()
            val tasksPerUser = tasks.groupingBy { task -> task.creatorId }.eachCount()

            ProjectSummaryReport(
                projectName = project.name,
                totalTasks = totalTasks,
                tasksPerState = tasksPerState,
                tasksPerUser = tasksPerUser
            )
        }
    }
}

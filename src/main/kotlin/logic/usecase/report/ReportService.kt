package org.baghdad.logic.usecase.report
import org.baghdad.logic.model.entities.ProjectSummaryReport
import org.baghdad.logic.model.exceptions.EmptyProjectSummaryReportException
import org.baghdad.logic.model.exceptions.EmptyProjectsException
import org.baghdad.logic.repositories.ProjectRepository
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.TaskRepository

class ReportService (
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository,
    private val stateRepository: ProjectStatesRepository
) {
    fun summary(): List<ProjectSummaryReport> {
        val projects = projectRepository.getAllProjects()

        if (projects.isEmpty()) throw EmptyProjectsException("no project found")

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

        }.takeIf { it.isNotEmpty() }?: throw EmptyProjectSummaryReportException("there is not summary report")
    }
}

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

        // إرجاع تقرير ملخص للمشاريع
        return projects.map { project ->
            // جلب المهام الخاصة بكل مشروع
            val tasks = taskRepository.getTasksByProjectId(project.id.toString())

            // حساب إجمالي عدد المهام
            val totalTasks = tasks.size

            // تجميع المهام حسب الحالة
            val tasksPerState = tasks.groupingBy { task -> task.stateId.toString() }.eachCount()

            // تجميع المهام حسب المستخدم (المستخدم الذي أنشأ المهمة)
            val tasksPerUser = tasks.groupingBy { task -> task.creatorId.toString() }.eachCount()

            // إنشاء تقرير يحتوي على المعلومات المطلوبة
            ProjectSummaryReport(
                projectName = project.name,  // تأكد من أن لديك حقل "name" في ProjectEntity
                totalTasks = totalTasks,
                tasksPerState = tasksPerState,
                tasksPerUser = tasksPerUser
            )
        }
    }
}

class ReportUseCase(
    private val reportService: ReportService
) {
    fun generateProjectSummary(): List<ProjectSummaryReport> {
        return reportService.summary()
    }
}
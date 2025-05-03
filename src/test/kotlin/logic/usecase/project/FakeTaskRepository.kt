package logic.usecase.project

import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.repositories.TaskRepository

class FakeTaskRepository : TaskRepository {
    private val tasks = mutableListOf<TaskEntity>()

    override fun createTask(task: TaskEntity) {
        tasks.add(task)
    }

    override fun getTaskById(id: String): TaskEntity {
        return tasks.firstOrNull { it.id.toString() == id }
            ?: throw NoSuchElementException("Task with ID $id not found.")
    }

    override fun getTasksByProjectId(projectId: String): List<TaskEntity> {
        return tasks.filter { it.projectId == projectId }
    }

    override fun getTasksByStateId(id: String): List<TaskEntity> {
        return tasks.filter { it.stateId == id }
    }

    override fun updateTask(task: TaskEntity): Boolean {
        val index = tasks.indexOfFirst { it.id == task.id }
        return if (index != -1) {
            tasks[index] = task
            true
        } else {
            false
        }
    }

    override fun deleteTask(taskId: String) {
        tasks.removeAll { it.id.toString() == taskId }
    }

    override fun getAllTasks(): List<TaskEntity> {
        return tasks.toList()
    }
}

package helpers.task

import org.baghdad.logic.model.entities.TaskEntity
import java.util.*

object TaskEntityTestData {

    val normalTask = createTaskHelper(
        title = "Task Management",
        description = "This is task due thursday for the chance program",
    )

    val tasksInSameProject = listOf(
        normalTask,
        normalTask.copy(
            title = "Plan mate",
        ),
        normalTask.copy(
            title = "Food App",
        )
    )

    val tasks = listOf(
        tasksInSameProject,
        listOf(
            normalTask.copy(
                projectId = UUID.randomUUID(),
                stateId = UUID.randomUUID()
            )
        )
    ).flatten()

    val randomTask = normalTask.copy(
        projectId = UUID.randomUUID(),
        stateId = UUID.randomUUID()
    )

    val tasksInSameState = listOf(
        normalTask,
        normalTask.copy(
            title = "Plan mate",
        ),
        normalTask.copy(
            title = "Food App",
        )
    )

    fun taskWithBlankTitle() = createTaskHelper(
        title = "     ",
        description = "This is task due thursday for the chance program",
    )

    fun taskWithBlankDescription() = createTaskHelper(
        title = "Task Management",
        description = "     ",
    )

    private fun createTaskHelper(
        title: String = "",
        description: String = "",
        stateId: UUID = UUID.randomUUID(),
        projectId: UUID = UUID.randomUUID(),
        creatorId: UUID = UUID.randomUUID()
    ) = TaskEntity(
        title = title,
        description = description,
        stateId = stateId,
        projectId = projectId,
        creatorId = creatorId
    )
}

object TaskTestData {
    fun taskWithState(stateId: UUID = UUID.randomUUID()) = TaskEntity(
        id = UUID.randomUUID(),
        title = "Task for $stateId",
        stateId = stateId,
        description = "Task description",
        projectId = UUID.randomUUID(),
        creatorId = UUID.randomUUID(),
    )
}
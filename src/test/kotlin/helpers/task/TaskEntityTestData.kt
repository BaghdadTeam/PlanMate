package helpers.task

import org.baghdad.logic.model.entities.TaskEntity

object TaskEntityTestData {

    val normalTask = createTaskHelper(
        title = "Task Management",
        description = "This is task due thursday for the chance program",
        stateId = "3",
        projectId = "1",
        creatorId = "50012"
    )

    val tasksInSameProject = listOf(
        normalTask,
        normalTask.copy(
            title = "Plan mate",
            stateId = "3",
        ),
        normalTask.copy(
            title = "Food App",
            stateId = "2",
        )
    )

    val tasks = listOf(
        tasksInSameProject,
        listOf(
            normalTask.copy(
                projectId = "3",
                stateId = "5"
            )
        )
    ).flatten()

    val randomTask = normalTask.copy(
        projectId = "99",
        stateId = "500"
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

    fun taskWithEmptyTitle() = createTaskHelper(
        description = "This is task due thursday for the chance program",
        stateId = "123123",
        projectId = "321321",
        creatorId = "50012"
    )

    fun taskWithBlankTitle() = createTaskHelper(
        title = "     ",
        description = "This is task due thursday for the chance program",
        stateId = "123123",
        projectId = "321321",
        creatorId = "50012"
    )

    fun taskWithEmptyDescription() = createTaskHelper(
        title = "Task Management",
        stateId = "123123",
        projectId = "321321",
        creatorId = "50012"
    )

    fun taskWithBlankDescription() = createTaskHelper(
        title = "Task Management",
        description = "     ",
        stateId = "123123",
        projectId = "321321",
        creatorId = "50012"
    )

    fun taskWithEmptyStateId() = createTaskHelper(
        title = "Task Management",
        description = "This is task due thursday for the chance program",
        projectId = "321321",
        creatorId = "50012"
    )

    fun taskWithBlankStateId() = createTaskHelper(
        title = "Task Management",
        description = "This is task due thursday for the chance program",
        projectId = "321321",
        stateId = "    ",
        creatorId = "50012"
    )

    fun taskWithEmptyProjectId() = createTaskHelper(
        title = "Task Management",
        description = "This is task due thursday for the chance program",
        stateId = "123123",
        creatorId = "50012"
    )

    fun taskWithBlankProjectId() = createTaskHelper(
        title = "Task Management",
        description = "This is task due thursday for the chance program",
        stateId = "123123",
        projectId = "    ",
        creatorId = "50012"
    )

    fun taskWithEmptyCreatorId() = createTaskHelper(
        title = "Task Management",
        description = "This is task due thursday for the chance program",
        stateId = "123123",
        projectId = "321321",
    )

    fun taskWithBlankCreatorId() = createTaskHelper(
        title = "Task Management",
        description = "This is task due thursday for the chance program",
        stateId = "123123",
        projectId = "321321",
        creatorId = "    "
    )

    private fun createTaskHelper(
        title: String = "",
        description: String = "",
        stateId: String = "",
        projectId: String = "",
        creatorId: String = ""
    ) = TaskEntity(
        title = title,
        description = description,
        stateId = stateId,
        projectId = projectId,
        creatorId = creatorId
    )
}
package helpers.task

import org.baghdad.logic.entities.TaskEntity

object TaskEntityTestData {

    fun normalTask() = createTaskHelper(
        title = "Task Management",
        description = "This is task due thursday for the chance program",
        stateId = "123123",
        projectId = "321321",
        creatorId = "50012"
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
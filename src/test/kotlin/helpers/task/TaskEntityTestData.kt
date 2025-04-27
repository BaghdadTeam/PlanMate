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

    fun taskMissingTitle() = createTaskHelper(
        description = "This is task due thursday for the chance program",
        stateId = "123123",
        projectId = "321321",
        creatorId = "50012"
    )

    fun taskMissingDescription() = createTaskHelper(
        title = "Task Management",
        stateId = "123123",
        projectId = "321321",
        creatorId = "50012"
    )

    fun taskMissingStateId() = createTaskHelper(
        title = "Task Management",
        description = "This is task due thursday for the chance program",
        projectId = "321321",
        creatorId = "50012"
    )

    fun taskMissingProjectId() = createTaskHelper(
        title = "Task Management",
        description = "This is task due thursday for the chance program",
        stateId = "123123",
        creatorId = "50012"
    )

    fun taskMissingCreatorId() = createTaskHelper(
        title = "Task Management",
        description = "This is task due thursday for the chance program",
        stateId = "123123",
        projectId = "321321",
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
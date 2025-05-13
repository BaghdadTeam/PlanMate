package helpers.projectStates

import org.baghdad.logic.model.entities.TaskStateEntity
import java.util.UUID

object ProjectStatesEntityTestData {

    fun getAllStatesPerProject() =
        listOf<TaskStateEntity>(
            todoState(), inProgressState(), inReviewState(), readyState(), doneState()
        )


    fun todoState() = createProjectStateHelper(
        name = "TODO",
    )

    fun inProgressState() = createProjectStateHelper(
        name = "In Progress",
    )


    fun inReviewState() = createProjectStateHelper(
        name = "Ready For Review",
    )

    fun readyState() = createProjectStateHelper(
        name = "Ready For Merge",
    )


    fun doneState() = createProjectStateHelper(
        name = "Done",
    )


    private fun createProjectStateHelper(
        name: String = "",
        projectId: UUID = UUID.randomUUID(),
        creatorId: UUID = UUID.randomUUID()
    ) = TaskStateEntity(
        name = name ,
        projectId = projectId,
        creatorId = creatorId
    )
}
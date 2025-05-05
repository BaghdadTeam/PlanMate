package helpers.projectStates

import org.baghdad.logic.model.entities.StateEntity
import java.util.UUID

object ProjectStatesEntityTestData {

    fun getAllStatesPerProject() =
        listOf<StateEntity>(
            todoState(), inProgressState(), inReviewState(), readyState(), doneState()
        )

    fun getStatesAfterDelete() =
        listOf<StateEntity>(
            todoState(), inProgressState(), inReviewState(), readyState()
        )

    fun todoState() = createProjectStateHelper(
        name = "TODO",


    )

    fun inProgressState() = createProjectStateHelper(
        name = "In Progress",

    )

    fun doingState() = createProjectStateHelper(
        name = "Doing",

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
    ) = StateEntity(
        name = name ,
        projectId = projectId,
        creatorId = creatorId
    )
}
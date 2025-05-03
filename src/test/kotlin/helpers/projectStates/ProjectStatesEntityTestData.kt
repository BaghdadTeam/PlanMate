package helpers.projectStates

import org.baghdad.logic.model.entities.StateEntity

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
        projectId = "123",
        creatorId = "5255"
    )

    fun inProgressState() = createProjectStateHelper(
        name = "In Progress",
        projectId = "123",
        creatorId = "5255"
    )

    fun doingState() = createProjectStateHelper(
        name = "Doing",
        projectId = "123",
        creatorId = "5255"
    )

    fun inReviewState() = createProjectStateHelper(
        name = "Ready For Review",
        projectId = "123",
        creatorId = "5255"
    )

    fun readyState() = createProjectStateHelper(
        name = "Ready For Merge",
        projectId = "123",
        creatorId = "5255"
    )


    fun doneState() = createProjectStateHelper(
        name = "Done",
        projectId = "123",
        creatorId = "5255"
    )



    private fun createProjectStateHelper(
        name: String = "",
        projectId: String = "",
        creatorId: String = ""
    ) = StateEntity(
        name = name ,
        projectId = projectId,
        creatorId = creatorId
    )
}
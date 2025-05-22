package presentation.projectStates

import io.mockk.mockk
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.projectStates.AddTaskStateToProjectUI
import org.baghdad.presentation.projectStates.DeleteStateForProjectUI
import org.baghdad.presentation.projectStates.EditProjectStateUI
import org.baghdad.presentation.projectStates.GetAllStatesPerProjectUI
import org.baghdad.presentation.projectStates.ProjectStatesManagementUI
import org.junit.jupiter.api.BeforeEach

class ProjectStatesManagementUITest {
    private lateinit var addStateToProjectUI: AddTaskStateToProjectUI
    private lateinit var deleteStateForProjectUI: DeleteStateForProjectUI
    private lateinit var editProjectStateUI: EditProjectStateUI
    private lateinit var getAllStatesPerProjectUI: GetAllStatesPerProjectUI
    private lateinit var projectStatesUI: ProjectStatesManagementUI
    private lateinit var reader: Reader
    private lateinit var viewer: Viewer

    @BeforeEach
    fun setUp() {
        reader = mockk()
        viewer = mockk()
        addStateToProjectUI = mockk()
        deleteStateForProjectUI = mockk()
        editProjectStateUI = mockk()
        getAllStatesPerProjectUI = mockk()

        projectStatesUI = ProjectStatesManagementUI(
            addStateToProjectUI,
            deleteStateForProjectUI,
            editProjectStateUI,
            getAllStatesPerProjectUI,
            reader,
            viewer
        )

    }
}
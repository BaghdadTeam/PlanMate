package presentation.projectStates

import io.mockk.mockk
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.projectStates.*
import org.junit.jupiter.api.BeforeEach


class ProjectStatesUITest {
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
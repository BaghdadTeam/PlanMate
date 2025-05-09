package org.baghdad.presentation.swimlane

import org.baghdad.presentation.projectStates.ProjectStatesUI
import java.util.UUID

class SwimlaneUI(
    private val renderSwimlaneUI: RenderSwimlaneUI
) {
    fun invoke(projectId : UUID){
        renderSwimlaneUI(projectId)

    }
}
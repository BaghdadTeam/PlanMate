package ui

import SwimlaneUI
import io.mockk.mockk
import io.mockk.verify
import io.mockk.Ordering
import org.baghdad.ui.*
import java.util.UUID
import kotlin.test.Test

class SwimlaneUITest {

    @Test
    fun `should handle all menu options including invalid inputs and exit properly`() {
        // give
        val inputs = listOf("1", "2", "3", "a", "9", "0").iterator()
        val projectId = UUID.randomUUID()

        val renderMock = mockk<RenderSwimlaneUI>(relaxed = true)
        val statesMock = mockk<ProjectStatesUI>(relaxed = true)
        val taskMock = mockk<TaskUI>(relaxed = true)
        val auditMock = mockk<AuditUI>(relaxed = true)

        val ui = SwimlaneUI(
            renderSwimlaneUI = renderMock,
            projectStatesUI = statesMock,
            taskUI = taskMock,
            auditUI = auditMock,
            inputProvider = { if (inputs.hasNext()) inputs.next() else null }
        )

        // when
        ui.invoke(projectId)

        // then
        verify(ordering = Ordering.SEQUENCE) {
            renderMock.invoke(projectId)
            statesMock.invoke(projectId)
            taskMock.invoke(projectId)
            auditMock.invoke(projectId)
        }
    }
}

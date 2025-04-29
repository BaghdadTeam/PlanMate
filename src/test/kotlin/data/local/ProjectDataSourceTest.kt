package data.local

import io.mockk.mockk
import io.mockk.verify
import org.baghdad.data.datasource.DataSource
import org.baghdad.data.local.ProjectDataSource
import org.baghdad.logic.model.entities.ProjectEntity
import kotlin.test.Test

class ProjectDataSourceTest {

    @Test
    fun `test createProject calls append on csvDataSource`() {
        // Given
        val mockDataSource = mockk<DataSource<ProjectEntity>>(relaxed = true)
        val projectDataSource = ProjectDataSource(mockDataSource)

        // When
        projectDataSource.createProject()

        // Then
        verify { mockDataSource.append(any()) }
    }
}
package data.local

import io.mockk.mockk
import io.mockk.verify
import org.baghdad.data.datasource.DataSource
import org.baghdad.data.local.CsvProjectDataSource
import org.baghdad.logic.model.entities.ProjectEntity
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class CsvProjectDataSourceTest {

    @Test
    fun `test createProject calls append on csvDataSource`() {
        // Given
        val mockDataSource = mockk<DataSource<ProjectEntity>>(relaxed = true)
        val csvProjectDataSource = CsvProjectDataSource(mockDataSource)

        // When
        csvProjectDataSource.createProject()

        // Then
        verify { mockDataSource.append(any()) }
    }
}
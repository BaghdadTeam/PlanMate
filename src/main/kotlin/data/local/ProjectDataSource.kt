package org.baghdad.data.local

import org.baghdad.data.datasource.DataSource
import org.baghdad.logic.model.entities.ProjectEntity
import java.util.UUID

class ProjectDataSource(
    private val csvDataSource: DataSource<ProjectEntity>
) {

    fun createProject() {
        csvDataSource.append(
            ProjectEntity(
                name = "Plane Mate",
                creatorId = UUID.fromString("rtwo' unq3fq3[rkom ")
            )
        )
    }
}
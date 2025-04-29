package org.baghdad.data.local

import org.baghdad.data.datasource.DataSource
import org.baghdad.logic.model.entities.ProjectEntity

class ProjectDataSource(
    private val csvDataSource: DataSource<ProjectEntity>
)
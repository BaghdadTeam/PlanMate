package org.baghdad.data.local

import org.baghdad.data.datasource.DataSource
import org.baghdad.logic.model.entities.TaskEntity

class TaskDataSource(
    private val csvDataSource: DataSource<TaskEntity>
)
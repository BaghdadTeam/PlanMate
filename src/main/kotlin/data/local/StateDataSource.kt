package org.baghdad.data.local

import org.baghdad.data.datasource.DataSource
import org.baghdad.logic.model.entities.StateEntity

class StateDataSource(
    private val csvDataSource: DataSource<StateEntity>
)
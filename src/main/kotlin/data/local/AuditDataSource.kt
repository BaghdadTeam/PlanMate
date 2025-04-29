package org.baghdad.data.local

import org.baghdad.data.datasource.DataSource
import org.baghdad.logic.model.entities.AuditEntity

class AuditDataSource(
    private val csvDataSource: DataSource<AuditEntity>
) {
}
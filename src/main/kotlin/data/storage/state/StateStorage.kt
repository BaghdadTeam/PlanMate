package org.baghdad.data.storage.state

import org.baghdad.logic.entities.StateEntity
import org.baghdad.data.storage.base.BasicStorage
import org.baghdad.data.storage.base.DeletableStorage
import org.baghdad.data.storage.base.UpdatableStorage

interface StateStorage
    : BasicStorage<StateEntity>,
    DeletableStorage<StateEntity>,
    UpdatableStorage<StateEntity> {

    fun getStatesByProjectId(projectId: String): List<StateEntity>
}
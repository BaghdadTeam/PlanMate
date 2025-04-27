package org.baghdad.logic.storage.state

import org.baghdad.logic.entities.StateEntity
import org.baghdad.logic.storage.base.BasicStorage
import org.baghdad.logic.storage.base.DeletableStorage
import org.baghdad.logic.storage.base.UpdatableStorage

interface StateStorage
    : BasicStorage<StateEntity>,
    DeletableStorage<StateEntity>,
    UpdatableStorage<StateEntity> {

    fun getStatesByProjectId(projectId: String): List<StateEntity>
}
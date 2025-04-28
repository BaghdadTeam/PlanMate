package org.baghdad.data.storage.task

import org.baghdad.logic.entities.TaskEntity
import org.baghdad.data.storage.base.BasicStorage
import org.baghdad.data.storage.base.DeletableStorage
import org.baghdad.data.storage.base.UpdatableStorage

interface TaskStorage
    : BasicStorage<TaskEntity>,
    DeletableStorage<TaskEntity>,
    UpdatableStorage<TaskEntity> {

    fun getTasksByProjectId(projectId: String): List<TaskEntity>
    fun getTasksByState(stateId: String): List<TaskEntity> // For swimlane UI
}
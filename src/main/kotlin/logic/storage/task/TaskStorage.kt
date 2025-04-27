package org.baghdad.logic.storage.task

import org.baghdad.logic.entities.TaskEntity
import org.baghdad.logic.storage.base.BasicStorage
import org.baghdad.logic.storage.base.DeletableStorage
import org.baghdad.logic.storage.base.UpdatableStorage

interface TaskStorage
    : BasicStorage<TaskEntity>,
    DeletableStorage<TaskEntity>,
    UpdatableStorage<TaskEntity> {

    fun getTasksByProjectId(projectId: String): List<TaskEntity>
    fun getTasksByState(stateId: String): List<TaskEntity> // For swimlane UI
}
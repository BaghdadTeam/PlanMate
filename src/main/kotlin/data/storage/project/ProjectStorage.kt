package org.baghdad.data.storage.project

import org.baghdad.logic.entities.ProjectEntity
import org.baghdad.data.storage.base.BasicStorage
import org.baghdad.data.storage.base.DeletableStorage
import org.baghdad.data.storage.base.UpdatableStorage

interface ProjectStorage
    : BasicStorage<ProjectEntity>,
    UpdatableStorage<ProjectEntity>,
    DeletableStorage<ProjectEntity>
package org.baghdad.logic.storage.project

import org.baghdad.logic.entities.ProjectEntity
import org.baghdad.logic.storage.base.BasicStorage
import org.baghdad.logic.storage.base.DeletableStorage
import org.baghdad.logic.storage.base.UpdatableStorage

interface ProjectStorage
    : BasicStorage<ProjectEntity>,
    UpdatableStorage<ProjectEntity>,
    DeletableStorage<ProjectEntity>
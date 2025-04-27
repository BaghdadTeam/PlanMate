package org.baghdad.logic.storage.base

interface UpdatableStorage<T>
    : BasicStorage<T> {

    fun update(id: String, updatedItem: T): Boolean
}
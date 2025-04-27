package org.baghdad.logic.storage.base

interface DeletableStorage<T>
    : BasicStorage<T> {

    fun delete(id: String)
}
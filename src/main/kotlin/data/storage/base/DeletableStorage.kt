package org.baghdad.data.storage.base

interface DeletableStorage<T>
    : BasicStorage<T> {

    fun delete(id: String)
}
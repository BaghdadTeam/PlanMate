package org.baghdad.logic.storage.base

interface BasicStorage<T> {
    fun save(item: T): Boolean
    fun read(id: String): T?
    fun getAll(): List<T>
}
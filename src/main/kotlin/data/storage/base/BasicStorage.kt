package org.baghdad.data.storage.base

interface BasicStorage<T> {
    fun save(item: T)
    fun read(id: String): T?
    fun getAll(): List<T>
}
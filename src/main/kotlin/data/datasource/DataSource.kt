package org.baghdad.data.datasource

interface DataSource<T> {
    suspend fun loadAll(): List<T>
    suspend fun update(item: T)
    suspend fun append(item: T)
    suspend fun delete(item: T)
}
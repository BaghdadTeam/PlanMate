package org.baghdad.data.datasource

interface DataSource<T> {
    fun loadAll(): List<T>
    fun saveAll(items: List<T>)
    fun append(item: T)
}
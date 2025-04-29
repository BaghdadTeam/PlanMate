package org.baghdad.data.datasource

interface DataSource<T> {
    fun loadAll(): List<T>
    fun update(items: List<T>)
    fun append(item: T)
}
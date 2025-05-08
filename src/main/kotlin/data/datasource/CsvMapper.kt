package org.baghdad.data.datasource

interface CsvMapper<T> {

    fun header(): String
    fun serializer(item: T): String
    fun deserializer(content: String): T
    fun getId(item: T): String // Unique identifier for update/delete
}
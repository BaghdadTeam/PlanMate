package org.baghdad.data.datasource

interface CsvMapper<T> {

    fun header(): String
    fun serializer(item: T): String
    fun deserializer(content: String): T
}
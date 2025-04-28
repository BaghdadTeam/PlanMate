package org.baghdad.data.datasource

interface CsvParser<T> {

    fun header(): String
    fun serializer(item: T): String
    fun deserializer(content: String): T
}
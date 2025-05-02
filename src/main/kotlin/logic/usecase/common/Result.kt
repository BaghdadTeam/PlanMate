package org.baghdad.logic.usecase.common

sealed class Result<out T> {
    data class Success<T>(val data: T? = null) : Result<T>()
    data class Failure(val message: String) : Result<Nothing>()
}
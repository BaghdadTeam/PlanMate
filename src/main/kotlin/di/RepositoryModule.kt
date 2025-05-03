package org.baghdad.di

import org.baghdad.data.repositories.task.TaskRepositoryImpl
import org.baghdad.logic.repositories.TaskRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<TaskRepository> { TaskRepositoryImpl(get()) }
}
package org.baghdad.di

import org.baghdad.data.repositories.projectstates.ProjectStatesRepositoryImp
import org.baghdad.data.repositories.task.TaskRepositoryImpl
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.TaskRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<TaskRepository> { TaskRepositoryImpl(get()) }
    single<ProjectStatesRepository> { ProjectStatesRepositoryImp(get()) }

}
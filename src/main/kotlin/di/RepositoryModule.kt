package org.baghdad.di

import org.baghdad.data.repositories.authentication.AuthenticationRepositoryImpl
import org.baghdad.data.repositories.authentication.SessionRepositoryImpl
import org.baghdad.data.repositories.projectstates.ProjectStatesRepositoryImp
import org.baghdad.data.repositories.task.TaskRepositoryImpl
import org.baghdad.logic.repositories.AuthenticationRepository
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.SessionRepository
import org.baghdad.logic.repositories.TaskRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<TaskRepository> { TaskRepositoryImpl(get()) }
    single<ProjectStatesRepository> { ProjectStatesRepositoryImp(get()) }
    single<SessionRepository> { SessionRepositoryImpl(get()) }
    single<AuthenticationRepository> { AuthenticationRepositoryImpl(get(), get()) }
}
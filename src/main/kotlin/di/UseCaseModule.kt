package org.baghdad.di

import org.baghdad.data.repositories.projectstates.ProjectStatesRepositoryImp
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.usecase.StateTransitionUseCase
import org.baghdad.logic.usecase.projectstate.AddStateToProjectUseCase
import org.baghdad.logic.usecase.task.*
import org.koin.dsl.module

val useCaseModule = module {

    // region  ::  Task Use Cases  ::

    single { StateTransitionUseCase(get(), get(), get()) }

    single { AddStateToProjectUseCase(get(), get(), get()) }
    single<ProjectStatesRepository> { ProjectStatesRepositoryImp(get()) }

    single { CreateTaskUseCase(get(), get(), get()) }
    single { DeleteTaskUseCase(get(), get(), get( )) }
    single { UpdateTaskUseCase(get(), get(), get()) }

    single { GetTasksByStateIdUseCase(get()) }
    single { GetTaskByIdUseCase(get()) }
    single { GetAllTasksUseCase(get()) }
    single { GetTasksByProjectIdUseCase(get()) }
    // endregion
}
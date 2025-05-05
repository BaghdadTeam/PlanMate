package org.baghdad.di

import org.baghdad.logic.usecase.StateTransitionUseCase
import org.baghdad.logic.usecase.project.CreateProjectUseCase
import org.baghdad.logic.usecase.project.DeleteProjectUseCase
import org.baghdad.logic.usecase.project.EditProjectUseCase
import org.baghdad.logic.usecase.project.ListProjectsUseCase
import org.baghdad.logic.usecase.task.*
import org.koin.dsl.module

val useCaseModule = module {

    // region  ::  Task Use Cases  ::

    single { StateTransitionUseCase(get(), get(), get()) }
    single { CreateTaskUseCase(get(), get(), get()) }
    single { DeleteTaskUseCase(get(), get(), get()) }
    single { UpdateTaskUseCase(get(), get(), get()) }

    single { GetTasksByStateIdUseCase(get()) }
    single { GetTaskByIdUseCase(get()) }
    single { GetAllTasksUseCase(get()) }
    single { GetTasksByProjectIdUseCase(get()) }

    // region  ::  Project Use Cases  ::

    single { CreateProjectUseCase(get()) }
    single { DeleteProjectUseCase(get(), get()) }
    single { EditProjectUseCase(get()) }
    single { ListProjectsUseCase(get()) }
    // endregion
}
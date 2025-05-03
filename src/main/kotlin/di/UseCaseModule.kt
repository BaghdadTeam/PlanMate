package org.baghdad.di

import org.baghdad.logic.usecase.StateTransitionUseCase
import org.baghdad.logic.usecase.task.*
import org.koin.dsl.module

val useCaseModule = module {

    // region  ::  Task Use Cases  ::

    single { StateTransitionUseCase(get(), get(), get()) }
    single { CreateTaskUseCase(get(), get(), get()) }
    single { DeleteTaskUseCase(get(), get()) }
    single { UpdateTaskUseCase(get(), get(), get()) }

    single { GetTasksByStateIdUseCase(get()) }
    single { GetTaskByIdUseCase(get()) }
    single { GetAllTasksUseCase(get()) }
    single { GetTasksByProjectIdUseCase(get()) }
    // endregion
}
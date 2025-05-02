package org.baghdad.di

import org.baghdad.logic.usecase.StateTransitionUseCase
import org.baghdad.logic.usecase.task.CreateTaskUseCase
import org.baghdad.logic.usecase.task.GetAllTasksUseCase
import org.koin.dsl.module

val useCaseModule = module {

    // region  ::  Task Use Cases  ::

    single { StateTransitionUseCase(get(), get(), get()) }

    single { GetAllTasksUseCase(get()) }
    single { CreateTaskUseCase(get(), get()) }

    // endregion
}
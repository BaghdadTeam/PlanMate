package org.baghdad.di

import org.baghdad.logic.usecase.StateTransitionUseCase
import org.baghdad.logic.usecase.authentication.LoginUseCase
import org.baghdad.logic.usecase.authentication.LogoutUseCase
import org.baghdad.logic.usecase.projectstates.*
import org.baghdad.logic.usecase.task.*
import org.baghdad.logic.usecase.user.CreateUserUseCase
import org.baghdad.logic.usecase.user.GetUserByUsernameUseCase
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
    // endregion

    // region  ::  Project States Use Cases  ::

    single { AddStateToProjectUseCase(get(), get(), get()) }
    single { DeleteStateForProjectUseCase(get(), get(), get()) }
    single { GetAllStatesPerProjectUseCase(get()) }
    single { GetStateByIdUseCase(get()) }
    single { EditProjectStatesUseCase(get(), get(), get()) }

    // endregion

    // region  ::  Authentication Use Cases  ::

    single { LoginUseCase(get(), get(), get()) }
    single { LogoutUseCase(get()) }

    // endregion

    // region  ::  User Management  Use Cases  ::
    single { CreateUserUseCase(get()) }
    single { GetUserByUsernameUseCase(get()) }
    // endregion
}
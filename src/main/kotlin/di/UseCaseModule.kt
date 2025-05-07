package org.baghdad.di

import org.baghdad.logic.usecase.StateTransitionUseCase
import org.baghdad.logic.usecase.project.CreateProjectUseCase
import org.baghdad.logic.usecase.project.DeleteProjectUseCase
import org.baghdad.logic.usecase.project.EditProjectUseCase
import org.baghdad.logic.usecase.project.GetAllProjectsUseCase
import org.baghdad.logic.usecase.task.CreateTaskUseCase
import org.baghdad.logic.usecase.task.DeleteTaskUseCase
import org.baghdad.logic.usecase.task.GetAllTasksUseCase
import org.baghdad.logic.usecase.task.GetTaskByIdUseCase
import org.baghdad.logic.usecase.task.GetTasksByProjectIdUseCase
import org.baghdad.logic.usecase.task.GetTasksByStateIdUseCase
import org.baghdad.logic.usecase.task.UpdateTaskUseCase
import org.koin.dsl.module

val useCaseModule = module {

    // region  ::  Task Use Cases  ::

    single { StateTransitionUseCase(get(), get(), get()) }
    single { CreateTaskUseCase(get(), get(), get()) }
    single { DeleteTaskUseCase(get(), get(), get()) }
    single { UpdateTaskUseCase(get(), get(), get()) }

    single { GetTasksByStateIdUseCase(get()) }
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

    // region  ::  Project Use Cases  ::

    single { CreateProjectUseCase(get(), get()) }
    single { DeleteProjectUseCase(get(), get()) }
    single { EditProjectUseCase(get(), get()) }
    single { GetAllProjectsUseCase(get()) }
    // endregion
}
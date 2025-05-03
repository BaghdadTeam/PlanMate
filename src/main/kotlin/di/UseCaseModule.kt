package org.baghdad.di

import org.baghdad.logic.usecase.StateTransitionUseCase
import org.baghdad.logic.usecase.project.CreateProjectUseCase
import org.baghdad.logic.usecase.project.DeleteProjectUseCase
import org.baghdad.logic.usecase.project.EditProjectUseCase
import org.baghdad.logic.usecase.project.ListProjectsUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { StateTransitionUseCase(get(), get(), get()) }
    single { CreateProjectUseCase(get()) }
    single { EditProjectUseCase(get()) }
    single { DeleteProjectUseCase(get(), get()) }
    single { ListProjectsUseCase(get()) }
}
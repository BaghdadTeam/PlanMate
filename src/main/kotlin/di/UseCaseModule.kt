package org.baghdad.di

import org.baghdad.logic.usecase.StateTransitionUseCase
import org.baghdad.logic.usecase.ProjectsServicesUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { StateTransitionUseCase(get(), get(), get()) }
    single { ProjectsServicesUseCase(get(), get()) }
}

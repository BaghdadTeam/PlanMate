package org.baghdad.di

import org.baghdad.logic.usecase.StateTransitionUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { StateTransitionUseCase(get(), get(), get()) }
}
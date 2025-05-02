package org.baghdad.di

import org.baghdad.presentation.Console
import org.baghdad.presentation.SystemConsole
import org.baghdad.presentation.PlanMateCLI
import org.baghdad.presentation.user.CreateUserUI
import org.baghdad.presentation.user.GetUserUI
import org.koin.dsl.module

val presentationModule = module {
    single<Console> { SystemConsole() }
    factory { CreateUserUI(get(), get()) }
    factory { GetUserUI(get(), get()) }
    single { PlanMateCLI(get(), get(), get()) }
}
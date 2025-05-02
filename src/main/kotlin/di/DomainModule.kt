package org.baghdad.di

import org.baghdad.logic.usecase.user.CreateUserUseCase
import org.baghdad.logic.usecase.user.GetUserByUsernameUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { CreateUserUseCase(get()) }
    factory { GetUserByUsernameUseCase(get()) }
}
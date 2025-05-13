package org.baghdad.di

import org.baghdad.data.repositories.authentication.TokenProviderImpl
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.repositories.TokenProvider
import org.baghdad.presentation.app.StartApp
import org.baghdad.presentation.app.ViewMainManu
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.input.ReaderImpl
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.output.ViewerImpl
import org.koin.dsl.module

val appModule = module {

    single<TokenProvider> { TokenProviderImpl() }
    single<Viewer> { ViewerImpl() }
    single<Reader> { ReaderImpl() }
    single { StartApp(get(), get(), get(), get()) }
    single { ViewMainManu(get(), get(), get(), get(), get(),get()) }
    single { SessionManager(get()) }
}
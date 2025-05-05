package org.baghdad.di

import org.baghdad.presentation.app.ConsoleUI
import org.baghdad.presentation.app.Feature
import org.baghdad.presentation.authentication.LoginUi
import org.baghdad.presentation.authentication.LogoutUi
import org.baghdad.presentation.projectStates.AddStateToProjectUI
import org.baghdad.presentation.projectStates.EditProjectStateUI
import org.baghdad.presentation.projectStates.GetAllStatesPerProjectUI
import org.baghdad.presentation.projectStates.GetStateByIdUI
import org.baghdad.presentation.task.*
import org.koin.dsl.bind
import org.koin.dsl.module

val uiModule = module {

    // region  :: TASKS ::
    factory { CreateTaskUI(get(), get(), get(), get()) } bind Feature::class
    factory { DeleteTaskUI(get(), get(), get(), get()) } bind Feature::class
    factory { GetTasksByStateIdUI(get(), get(), get()) } bind Feature::class
    factory { UpdateTaskUI(get(), get(), get(), get()) } bind Feature::class
    factory { GetTaskByIdUI(get(), get(), get()) } bind Feature::class
    // endregion

    // region  :: PROJECT STATES ::
    factory { AddStateToProjectUI(get(), get(), get(), get()) } bind Feature::class
    factory { EditProjectStateUI(get(), get(), get(), get()) } bind Feature::class
    factory { GetAllStatesPerProjectUI(get(), get(), get()) } bind Feature::class
    factory { GetStateByIdUI(get(), get(), get()) } bind Feature::class
    // endregion

    // region  :: AUTH ::
    factory { LoginUi(get(), get(), get()) } // not a menu option
    factory { LogoutUi(get(), get(), get()) } bind Feature::class
    // endregion


    factory<Map<Int, Feature>> {
        getAll<Feature>().associateBy { it.id }
    }

    single { ConsoleUI(get(), get(), get()) }
}
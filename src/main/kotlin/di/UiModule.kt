package org.baghdad.di

import org.baghdad.logic.usecase.projectstates.DeleteStateForProjectUseCase
import org.baghdad.presentation.project.CreateProjectUi
import org.baghdad.presentation.project.DeleteProjectUi
import org.baghdad.presentation.project.ListProjectUi
import org.baghdad.presentation.projectStates.AddStateToProjectUI
import org.baghdad.presentation.projectStates.EditProjectStateUI
import org.baghdad.presentation.projectStates.GetAllStatesPerProjectUI
import org.baghdad.presentation.projectStates.GetStateByIdUI
import org.baghdad.presentation.task.*
import org.koin.dsl.module

val uiModule = module {

    single { CreateTaskUI(get(), get(), get(), get()) }
    single { DeleteTaskUI(get(), get(), get(), get()) }
    single { GetTasksByStateIdUI(get(), get(), get()) }
    single { GetTasksByStateIdUI(get(), get(), get()) }
    single { UpdateTaskUI(get(), get(), get(), get()) }
    // endregion

    // region  :: PROJECT STATES ::

    single { AddStateToProjectUI(get(), get(), get(), get()) }
    single { EditProjectStateUI(get(), get(), get(), get()) }
    single { DeleteStateForProjectUseCase(get(), get(), get()) }
    single { GetAllStatesPerProjectUI(get(), get(), get()) }
    single { GetStateByIdUI(get(), get(), get()) }

    // endregion

    // --------------------ProjectUi----------------------
    single { CreateProjectUi(get()) }
    single { DeleteProjectUi(get()) }
    single { ListProjectUi(get()) }
}
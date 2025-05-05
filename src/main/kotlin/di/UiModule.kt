package org.baghdad.di

import org.baghdad.logic.usecase.projectstates.DeleteStateForProjectUseCase
import org.baghdad.presentation.authentication.LoginUi
import org.baghdad.presentation.authentication.LogoutUi
import org.baghdad.presentation.projectStates.AddStateToProjectUI
import org.baghdad.presentation.projectStates.EditProjectStateUI
import org.baghdad.presentation.projectStates.GetAllStatesPerProjectUI
import org.baghdad.presentation.projectStates.GetStateByIdUI
import org.baghdad.presentation.task.*
import org.baghdad.presentation.user.CreateUserUI
import org.baghdad.presentation.user.GetUserByUsernameUI
import org.koin.dsl.module

val uiModule = module {

    // region  :: TASKS ::
    single { CreateTaskUI(get(), get(), get(), get()) }
    single { DeleteTaskUI(get(), get(), get(), get()) }
    single { GetTasksByStateIdUI(get(), get(), get()) }
    single { GetTasksByStateIdUI(get(), get(), get()) }
    single { UpdateTaskUI(get(), get(), get(), get()) }
    single { GetTaskByIdUI(get(), get(), get()) }
    // endregion

    // region  :: PROJECT STATES ::

    single { AddStateToProjectUI(get(), get(), get(), get()) }
    single { EditProjectStateUI(get(), get(), get(), get()) }
    single { DeleteStateForProjectUseCase(get(), get(), get()) }
    single { GetAllStatesPerProjectUI(get(), get(), get()) }
    single { GetStateByIdUI(get(), get(), get()) }

    // endregion

    // region  :: PROJECTS ::
    // endregion

    // region  :: Auth ::
    single { LoginUi(get(), get(), get()) }
    single { LogoutUi(get(), get(), get()) }
    // endregion

    // region  :: User Management ::
    single { CreateUserUI(get(),get(),get()) }
    single { GetUserByUsernameUI(get(),get(),get()) }
    single { GetStateByIdUI(get(),get(),get()) }
    // endregion
}
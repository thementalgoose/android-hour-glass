package tmg.hourglass.dashboard

import androidx.lifecycle.ViewModel

//region Inputs

interface DashboardViewModelInputs {

}

//endregion

//region Outputs

interface DashboardViewModelOutputs {

}

//endregion

class DashboardViewModel: ViewModel(), DashboardViewModelInputs, DashboardViewModelOutputs {

    val inputs: DashboardViewModelInputs = this
    val outputs: DashboardViewModelOutputs = this

}
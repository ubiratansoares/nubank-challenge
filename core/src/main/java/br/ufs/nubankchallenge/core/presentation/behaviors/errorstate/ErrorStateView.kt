package br.ufs.nubankchallenge.core.presentation.errorstate

import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError
import io.reactivex.functions.Action

/**
 *
 * Created by @ubiratanfsoares
 *
 */

interface ErrorStateView {

    fun showErrorState(error: InfrastructureError): Action

    fun hideErrorState(): Action

}
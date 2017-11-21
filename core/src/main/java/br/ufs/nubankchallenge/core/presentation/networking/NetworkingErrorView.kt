package br.ufs.nubankchallenge.core.presentation.networking

import br.ufs.nubankchallenge.core.domain.NetworkingIssue
import io.reactivex.functions.Action

/**
 *
 * Created by @ubiratanfsoares
 *
 */

interface NetworkingErrorView {

    fun onNetworkingError(issue: NetworkingIssue): Action

}
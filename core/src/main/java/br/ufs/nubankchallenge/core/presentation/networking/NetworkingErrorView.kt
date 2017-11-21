package br.ufs.nubankchallenge.core.presentation.networking

import br.ufs.nubankchallenge.core.domain.errors.NetworkingIssue
import io.reactivex.functions.Action

/**
 *
 * Created by @ubiratanfsoares
 *
 */

interface NetworkingErrorView {

    fun reportNetworkingError(issue: NetworkingIssue): Action

}
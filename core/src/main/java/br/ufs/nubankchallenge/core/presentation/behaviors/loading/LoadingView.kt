package br.ufs.nubankchallenge.core.presentation.loading

import io.reactivex.functions.Action

/**
 *
 * Created by @ubiratanfsoares
 *
 */

interface LoadingView {

    fun showLoading(): Action

    fun hideLoading(): Action

}
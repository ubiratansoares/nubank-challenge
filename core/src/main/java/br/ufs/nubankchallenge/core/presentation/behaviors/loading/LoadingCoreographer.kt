package br.ufs.nubankchallenge.core.presentation.loading

import io.reactivex.*
import io.reactivex.functions.Action

/**
 *
 * Created by @ubiratanfsoares
 *
 */

class LoadingCoreographer(val view: Any,
                          val uiScheduler: Scheduler) : ObservableTransformer<Any, Any> {

    override fun apply(upstream: Observable<Any>): ObservableSource<Any> {

        if (view is LoadingView) {
            return upstream
                    .subscribeOn(uiScheduler)
                    .doOnSubscribe { _ -> subscribeAndFireAction(view.showLoading()) }
                    .doOnTerminate { subscribeAndFireAction(view.hideLoading()) }
        }

        return upstream
    }

    private fun subscribeAndFireAction(toPerform: Action) {
        Completable.fromAction(toPerform)
                .subscribeOn(uiScheduler)
                .subscribe()
    }

}
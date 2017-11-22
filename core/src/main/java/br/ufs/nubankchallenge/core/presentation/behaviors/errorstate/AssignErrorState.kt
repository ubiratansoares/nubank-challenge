package br.ufs.nubankchallenge.core.presentation.errorstate

import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError
import io.reactivex.*
import io.reactivex.functions.Action

/**
 *
 * Created by @ubiratanfsoares
 *
 */
class AssignErrorState(val view: Any,
                       val uiScheduler: Scheduler) : ObservableTransformer<Any, Any> {

    override fun apply(upstream: Observable<Any>): ObservableSource<Any> {

        if (view is ErrorStateView) {
            return upstream
                    .subscribeOn(uiScheduler)
                    .doOnSubscribe { _ -> subscribeAndFireAction(view.hideErrorState()) }
                    .doOnError(this::evalute)

        }
        return upstream
    }

    private fun evalute(error: Throwable) {
        if (error is InfrastructureError) {
            subscribeAndFireAction((view as ErrorStateView).showErrorState(error))
        }
    }

    private fun subscribeAndFireAction(toPerform: Action) {
        Completable.fromAction(toPerform)
                .subscribeOn(uiScheduler)
                .subscribe()
    }
}
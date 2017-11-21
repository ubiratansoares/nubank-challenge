package br.ufs.nubankchallenge.core.presentation

import br.ufs.nubankchallenge.core.presentation.errorstate.AssignErrorState
import br.ufs.nubankchallenge.core.presentation.loading.LoadingCoreographer
import br.ufs.nubankchallenge.core.presentation.networking.NetworkingErrorFeedback
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler

/**
 *
 * Created by @ubiratanfsoares
 *
 */

class BehaviorsPresenter(
        private val view: Any,
        private val scheduler: Scheduler) : ObservableTransformer<Any, Any> {

    override fun apply(upstream: Observable<Any>): ObservableSource<Any> {

        return upstream
                .compose(AssignErrorState(view, scheduler))
                .compose(NetworkingErrorFeedback(view, scheduler))
                .compose(LoadingCoreographer(view, scheduler))
    }

}
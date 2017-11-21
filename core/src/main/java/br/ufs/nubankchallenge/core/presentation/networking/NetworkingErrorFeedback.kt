package br.ufs.nubankchallenge.core.presentation.networking

import br.ufs.nubankchallenge.core.domain.NetworkingIssue
import io.reactivex.*

/**
 *
 * Created by @ubiratanfsoares
 *
 */

class NetworkingErrorFeedback(
        val passiveView: Any,
        val uiScheduler: Scheduler) : ObservableTransformer<Any, Any> {

    override fun apply(upstream: Observable<Any>): ObservableSource<Any> {
        return upstream.doOnError { handleIfNetworkingError(it) }
    }

    private fun handleIfNetworkingError(throwable: Throwable): Observable<Any> {

        if (passiveView is NetworkingErrorView && throwable is NetworkingIssue) {
            Completable.fromAction(passiveView.reportNetworkingError(throwable))
                    .subscribeOn(uiScheduler)
                    .subscribe()
        }

        return Observable.error(throwable)
    }
}
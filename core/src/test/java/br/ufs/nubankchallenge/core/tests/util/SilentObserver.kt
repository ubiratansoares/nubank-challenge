package br.ufs.nubankchallenge.core.tests

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 *
 * Created by @ubiratanfsoares
 *
 * An degenerated observer to clean up our tests logs
 *
 */

object SilentObserver : Observer<Any> {

    override fun onComplete() {
        // No op
    }

    override fun onError(e: Throwable) {
        // No op
    }

    override fun onNext(t: Any) {
        // No op
    }

    override fun onSubscribe(d: Disposable) {
        // No op
    }
}
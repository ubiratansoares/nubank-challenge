package br.ufs.nubankchallenge.core.presentation.util

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.internal.operators.observable.ObservableNever

/**
 *
 * Created by @ubiratanfsoares
 *
 */

class Replayer<T> : ObservableTransformer<T, T> {

    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream
                .replay(BUFFER_COUNT)
                .autoConnect(MAX_SUBSCRIBERS)
    }

    companion object {
        val CLEAR_STATE: Observable<*> = ObservableNever.INSTANCE
        val MAX_SUBSCRIBERS = 1
        val BUFFER_COUNT = 1
    }
}
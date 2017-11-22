package br.ufs.nubankchallenge.core.presentation.notice

import android.arch.lifecycle.ViewModel
import br.ufs.nubankchallenge.core.domain.notice.RetrieveNotice
import io.reactivex.Observable
import io.reactivex.internal.operators.observable.ObservableNever

/**
 *
 * Created by @ubiratanfsoares
 *
 */

class NoticeScreen(private val usecase: RetrieveNotice) : ViewModel() {

    private var replayer: Observable<NoticeScreenModel> = Observable.never()

    fun retrieve(invalidate: Boolean = false): Observable<NoticeScreenModel> {
        if (invalidate) reset()
        if (replayer == CLEAR_STATE) replayer = assignReplayer()
        return replayer
    }

    private fun assignReplayer(): Observable<NoticeScreenModel> {
        return usecase
                .execute()
                .map { NoticeScreenModel(it) }
                .replay(BUFFER_COUNT)
                .autoConnect(MAX_SUBSCRIBERS)
    }

    private fun reset() {
        replayer = Observable.never()
    }

    companion object {
        val CLEAR_STATE: Observable<*> = ObservableNever.INSTANCE
        val MAX_SUBSCRIBERS = 1
        val BUFFER_COUNT = 1
    }
}
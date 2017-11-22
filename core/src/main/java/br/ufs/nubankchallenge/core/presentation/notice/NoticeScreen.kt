package br.ufs.nubankchallenge.core.presentation.notice

import android.arch.lifecycle.ViewModel
import br.ufs.nubankchallenge.core.domain.notice.RetrieveNotice
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.internal.operators.observable.ObservableNever
import io.reactivex.schedulers.Schedulers

/**
 *
 * Created by @ubiratanfsoares
 *
 */

class NoticeScreen(
        private val usecase: RetrieveNotice,
        private val uiScheduler: Scheduler = Schedulers.trampoline()) : ViewModel() {

    private var replayer: Observable<NoticeScreenModel> = Observable.never()

    fun retrieveNotice(invalidate: Boolean = false): Observable<NoticeScreenModel> {
        if (invalidate) reset()
        if (replayer == CLEAR_STATE) replayer = assignReplayer()
        return replayer
    }

    private fun assignReplayer(): Observable<NoticeScreenModel> {
        return usecase
                .execute()
                .map { NoticeScreenModel(it) }
                .observeOn(uiScheduler)
                .replay(BUFFER_COUNT)
                .autoConnect(MAX_SUBSCRIBERS)
    }

    private fun reset() {
        replayer = Observable.never()
    }

    private companion object {
        val CLEAR_STATE: Observable<*> = ObservableNever.INSTANCE
        val MAX_SUBSCRIBERS = 1
        val BUFFER_COUNT = 1
    }
}
package br.ufs.nubankchallenge.core.presentation.notice

import android.arch.lifecycle.ViewModel
import br.ufs.nubankchallenge.core.domain.notice.RetrieveNotice
import br.ufs.nubankchallenge.core.presentation.util.Replayer
import br.ufs.nubankchallenge.core.presentation.util.Replayer.Companion.CLEAR_STATE
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

/**
 *
 * Created by @ubiratanfsoares
 *
 */

class NoticeScreen(
        private val usecase: RetrieveNotice,
        private val uiScheduler: Scheduler = Schedulers.trampoline()) : ViewModel() {

    private var replayableOperation: Observable<NoticeScreenModel> = Observable.never()

    fun retrieveNotice(invalidate: Boolean = false): Observable<NoticeScreenModel> {
        if (invalidate) reset()
        if (replayableOperation == CLEAR_STATE) replayableOperation = assignReplayer()
        return replayableOperation
    }

    private fun assignReplayer(): Observable<NoticeScreenModel> {
        return usecase
                .execute()
                .map { NoticeScreenModel(it) }
                .observeOn(uiScheduler)
                .compose(Replayer())
    }

    private fun reset() {
        replayableOperation = Observable.never()
    }

}
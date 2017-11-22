package br.ufs.hiring.nubankchallenge.factories

import br.ufs.nubankchallenge.core.presentation.behaviors.BehaviorsPresenter
import br.ufs.nubankchallenge.core.presentation.notice.NoticeScreen
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 *
 * Created by @ubiratanfsoares
 *
 */

object PresentationFactory {

    fun behaviorsPresenter(view: Any): BehaviorsPresenter {
        return BehaviorsPresenter(view, AndroidSchedulers.mainThread())
    }

    fun noticeScreen(): NoticeScreen {
        return NoticeScreen(InfrastructureFactory.notice(), AndroidSchedulers.mainThread())
    }

}
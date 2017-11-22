package br.ufs.hiring.nubankchallenge.factories

import br.ufs.nubankchallenge.core.infrastructure.NoticeInfrastructure
import io.reactivex.schedulers.Schedulers

/**
 *
 * Created by @ubiratanfsoares
 *
 */

object InfrastructureFactory {

    fun notice(): NoticeInfrastructure {
        return NoticeInfrastructure(WebServiceFactory.webservice, Schedulers.io())
    }

}
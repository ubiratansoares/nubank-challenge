package br.ufs.nubankchallenge.core.domain.notice

import io.reactivex.Observable

/**
 *
 * Created by @ubiratanfsoares
 *
 */

interface RetrieveNotice {

    fun execute(): Observable<ChargebackNotice>

}
package br.ufs.nubankchallenge.core.domain.chargeback

import io.reactivex.Observable

/**
 *
 * Created by @ubiratanfsoares
 *
 */

interface RetrieveNotice {

    fun execute(): Observable<ChargebackNotice>

}
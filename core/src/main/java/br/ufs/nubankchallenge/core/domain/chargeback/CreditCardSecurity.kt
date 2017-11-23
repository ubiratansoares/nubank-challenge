package br.ufs.nubankchallenge.core.domain.chargeback

import io.reactivex.Observable

/**
 *
 * Created by @ubiratanfsoares
 *
 */

interface CreditCardSecurity {

    fun blockSolicitation(): Observable<Unit>
    fun unblockSoliction(): Observable<Unit>

}
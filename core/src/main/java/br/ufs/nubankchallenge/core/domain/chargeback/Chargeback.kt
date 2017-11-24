package br.ufs.nubankchallenge.core.domain.chargeback

import br.ufs.nubankchallenge.core.domain.chargeback.models.ChargebackOptions
import br.ufs.nubankchallenge.core.domain.chargeback.models.ChargebackReclaim
import io.reactivex.Observable

/**
 *
 * Created by @ubiratanfsoares
 *
 */
interface Chargeback {

    fun sendReclaim(reclaim: ChargebackReclaim) : Observable<Unit>

    fun possibleActions(): Observable<ChargebackOptions>

}
package br.ufs.nubankchallenge.core.domain.chargeback

import br.ufs.nubankchallenge.core.domain.chargeback.models.ChargebackOptions
import io.reactivex.Observable

/**
 *
 * Created by @ubiratanfsoares
 *
 */

interface RetrieveChargebackOptions {

    fun execute(): Observable<ChargebackOptions>

}
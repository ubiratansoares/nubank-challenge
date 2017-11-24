package br.ufs.nubankchallenge.core.domain.chargeback

import br.ufs.nubankchallenge.core.domain.chargeback.models.ChargebackOptions
import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError
import br.ufs.nubankchallenge.core.domain.errors.NetworkingIssue.InternetUnreachable
import io.reactivex.Observable
import io.reactivex.Observable.error
import io.reactivex.Observable.just
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

/**
 *
 * Created by @ubiratanfsoares
 *
 */

private typealias Options = ChargebackOptions

class PreventiveCardBlocking(
        private val blocker: CreditCardSecurity) : ObservableTransformer<Options, Options> {

    override fun apply(upstream: Observable<Options>): ObservableSource<Options> {

        return upstream.switchMap {
            if (it.shouldBlockCreditcard) tryBlockPreventively(it) else just(it)
        }
    }

    private fun tryBlockPreventively(options: Options) =
            blocker.blockSolicitation()
                    .map { options }
                    .onErrorResumeNext {
                        failure: Throwable -> recoverGracefully(failure, options)
                    }

    private fun recoverGracefully(
            failure: Throwable,
            options: Options): ObservableSource<Options> = when (failure) {
                is InfrastructureError -> recoverUsingOriginal(options)
                is InternetUnreachable -> recoverUsingOriginal(options)
                else -> error { failure }
            }

    private fun recoverUsingOriginal(original: Options) =
            just(original.copy(shouldBlockCreditcard = false))
}
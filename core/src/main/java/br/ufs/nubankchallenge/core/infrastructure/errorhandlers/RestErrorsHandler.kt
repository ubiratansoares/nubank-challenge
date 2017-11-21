package br.ufs.nubankchallenge.core.infrastructure.errorhandlers

import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError
import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError.RemoteSystemDown
import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError.UndesiredResponse
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import retrofit2.HttpException

/**
 *
 * Created by @ubiratanfsoares
 *
 */

class RestErrorsHandler : ObservableTransformer<Any, Any> {

    override fun apply(upstream: Observable<Any>): ObservableSource<Any> {
        return upstream.onErrorResumeNext(this::handleIfRestError)
    }

    private fun handleIfRestError(throwable: Throwable): Observable<Any> {
        if (isRestError(throwable)) return toInfrastructureError(throwable as HttpException)
        return Observable.error(throwable)
    }

    private fun toInfrastructureError(restError: HttpException): Observable<Any> {
        val infraError = mapErrorWith(restError.code())
        return Observable.error(infraError)
    }

    private fun mapErrorWith(code: Int): InfrastructureError {
        return when (code) {
            in 500..511 -> RemoteSystemDown
            else -> UndesiredResponse
        }
    }

    private fun isRestError(throwable: Throwable): Boolean {
        return throwable is HttpException
    }
}
package br.ufs.nubankchallenge.core.infrastructure.errorhandlers

import br.ufs.nubankchallenge.core.domain.errors.NetworkingIssue.*
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 *
 * Created by @ubiratanfsoares
 *
 */

class NetworkingErrorsHandler : ObservableTransformer<Any, Any> {

    override fun apply(upstream: Observable<Any>): ObservableSource<Any> {
        return upstream.onErrorResumeNext(this::handleIfNetworkingError)
    }

    private fun handleIfNetworkingError(throwable: Throwable): Observable<Any> {
        if (isNetworkingError(throwable)) return asNetworkingError(throwable)
        return Observable.error(throwable)
    }

    private fun asNetworkingError(throwable: Throwable): Observable<Any> {
        return Observable.error(mapToDomainError(throwable))
    }

    private fun mapToDomainError(throwable: Throwable): Throwable {

        if (isConnectionTimeout(throwable)) return OperationTimeout
        if (noInternetAvailable(throwable)) return InternetUnreachable
        return ConnectionSpike
    }

    private fun isNetworkingError(throwable: Throwable): Boolean {
        return isConnectionTimeout(throwable) ||
                noInternetAvailable(throwable) ||
                isRequestCanceled(throwable)
    }

    private fun isRequestCanceled(throwable: Throwable): Boolean {
        return throwable is IOException
                && throwable.message?.contentEquals("Canceled") ?: false
    }

    private fun noInternetAvailable(throwable: Throwable): Boolean {
        return throwable is UnknownHostException
    }

    private fun isConnectionTimeout(throwable: Throwable): Boolean {
        return throwable is SocketTimeoutException
    }

}
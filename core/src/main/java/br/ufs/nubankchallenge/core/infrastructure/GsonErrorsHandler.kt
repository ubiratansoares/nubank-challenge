package br.ufs.nubankchallenge.core.infrastructure

import br.ufs.nubankchallenge.core.domain.errors.InfrastructureError.UndesiredResponse
import com.google.gson.JsonIOException
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

/**
 *
 * Created by @ubiratanfsoares
 *
 */
class GsonErrorsHandler : ObservableTransformer<Any, Any> {

    override fun apply(upstream: Observable<Any>): ObservableSource<Any> {
        return upstream.onErrorResumeNext(this::handleErrorFromDeserializer)
    }

    private fun handleErrorFromDeserializer(throwable: Throwable): Observable<Any> {
        if (isErrorFromGson(throwable)) return Observable.error(UndesiredResponse)
        return Observable.error(throwable)
    }

    private fun isErrorFromGson(throwable: Throwable): Boolean {
        return when (throwable) {
            is JsonIOException -> true
            is JsonSyntaxException -> true
            is JsonParseException -> true
            else -> false
        }
    }
}
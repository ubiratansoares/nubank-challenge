package br.ufs.nubankchallenge.core.domain.errors

/**
 *
 * Created by @ubiratanfsoares
 *
 */

sealed class InfrastructureError : Throwable() {

    object RemoteSystemDown : InfrastructureError()
    object UndesiredResponse : InfrastructureError()
}
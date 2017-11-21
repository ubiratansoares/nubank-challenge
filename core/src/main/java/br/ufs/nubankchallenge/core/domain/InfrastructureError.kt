package br.ufs.nubankchallenge.core.domain

/**
 *
 * Created by @ubiratanfsoares
 *
 */

sealed class InfrastructureError : Throwable() {

    object ContentNotFound : InfrastructureError()
    object RemoteSystemDown : InfrastructureError()
    object UndesiredResponse : InfrastructureError()
}
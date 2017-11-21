package br.ufs.nubankchallenge.core.domain

/**
 *
 * Created by @ubiratanfsoares
 *
 */

sealed class InfrastructureError : Throwable() {

    object RemoteSystemDown : InfrastructureError()
    object UndesiredResponse : InfrastructureError()
}
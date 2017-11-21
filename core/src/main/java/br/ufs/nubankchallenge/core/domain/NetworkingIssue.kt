package br.ufs.nubankchallenge.core.domain

/**
 *
 * Created by @ubiratanfsoares
 *
 */

sealed class NetworkingIssue : Throwable() {
    object InternetUnreachable : NetworkingIssue()
    object OperationTimeout : NetworkingIssue()
    object ConnectionSpike : NetworkingIssue()
}
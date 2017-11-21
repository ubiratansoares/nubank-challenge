package br.ufs.nubankchallenge.core.domain.errors

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
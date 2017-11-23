package br.ufs.hiring.nubankchallenge.factories

/**
 *
 * Created by @ubiratanfsoares
 *
 */
sealed class TestScenario {

    object NetworkingHeadache : TestScenario()
    object ServersDown : TestScenario()
    object ClientError : TestScenario()
    object ChargebackNoticeRetrieved : TestScenario()

}
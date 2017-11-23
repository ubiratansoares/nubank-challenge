package br.ufs.nubankchallenge.core.domain.chargeback.models

/**
 *
 * Created by @ubiratanfsoares
 *
 */

data class ChargebackReclaim(val userHistory: String, val frauds: List<Fraud>)
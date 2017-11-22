package br.ufs.nubankchallenge.core.domain.notice

/**
 *
 * Created by @ubiratanfsoares
 *
 */

data class ChargebackNotice(
        val title: String,
        val rawDescription: String,
        val primaryActionText: String,
        val secondaryActionText: String
)
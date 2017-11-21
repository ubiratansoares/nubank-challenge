package br.ufs.hiring.nubankchallenge.factories

import br.ufs.nubankchallenge.core.PurchaseTransaction
import java.util.*

/**
 *
 * Created by @ubiratanfsoares
 *
 */

object FakeTransactionFactory {

    operator fun invoke(): PurchaseTransaction {
        val randomId = UUID.randomUUID().toString()
        val randomVenue = venues[randomizer.nextInt(venues.size)]
        val purchaseValue = "R$ " + (randomizer.nextInt(1000) + 100) + ",00"

        val randomDateTime =
                "${randomizer.nextInt(20) + 10}" +
                        " NOV 2017, " +
                        "${randomizer.nextInt(13) + 10}:" +
                        "${randomizer.nextInt(50) + 10}"

        return PurchaseTransaction(
                id = randomId,
                venueName = randomVenue,
                formattedDateTime = randomDateTime,
                formattedValue = purchaseValue
        )
    }

    private val venues = listOf(
            "Sonda Supermercados",
            "Extra Hipermercados",
            "Mercadinho Meteoro",
            "Supermercados Yamauchi",
            "Praça dos Pães"

    )

    private val randomizer = Random()

}
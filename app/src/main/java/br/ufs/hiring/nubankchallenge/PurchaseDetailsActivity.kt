package br.ufs.hiring.nubankchallenge

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.ufs.hiring.nubankchallenge.factories.FakeTransactionFactory
import kotlinx.android.synthetic.main.activity_purchase_details.*
import nubank.hiring.ufs.br.nubankchallenge.R

class PurchaseDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_details)
        fillWithFakeData()
    }

    private fun fillWithFakeData() {
        val transaction = FakeTransactionFactory()

        labelPurchaseDateTime.text = transaction.formattedDateTime
        labelVenueName.text = transaction.venueName
        labelFormattedValue.text = transaction.formattedValue
    }
}

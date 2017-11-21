package br.ufs.hiring.nubankchallenge

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_purchase_details.*
import nubank.hiring.ufs.br.nubankchallenge.R

class PurchaseDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_details)
        fillWithFakeData()
    }

    private fun fillWithFakeData() {
        labelPurchaseDateTime.text = "30 FEV 2017, 22:00"
        labelVenueName.text = "Sonda Supermercados"
        labelFormattedValue.text = "R$99,10"
    }
}

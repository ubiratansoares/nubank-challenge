package br.ufs.hiring.nubankchallenge.transaction

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.ufs.hiring.nubankchallenge.R
import br.ufs.hiring.nubankchallenge.factories.FakeTransaction
import br.ufs.hiring.nubankchallenge.notice.NoticeActivity
import kotlinx.android.synthetic.main.activity_purchase_details.*

class PurchaseDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_details)
        fillWithFakeData()

        startChargebackButton.setOnClickListener {
            startActivity(Intent(baseContext, NoticeActivity::class.java))
        }
    }

    private fun fillWithFakeData() {
        val transaction = FakeTransaction()
        labelPurchaseDateTime.text = transaction.formattedDateTime
        labelVenueName.text = transaction.venueName
        labelFormattedValue.text = transaction.formattedValue
    }
}

package com.vsantander.paymentchallenge.presentation.amountselector

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.vsantander.paymentchallenge.R
import com.vsantander.paymentchallenge.presentation.base.activity.BaseActivity
import com.vsantander.paymentchallenge.presentation.summary.SummaryActivity
import com.vsantander.paymentchallenge.utils.extension.afterTextChange
import kotlinx.android.synthetic.main.activity_amount_selector.*
import com.vsantander.paymentchallenge.presentation.view.InputFilterMinMax
import android.text.InputFilter


@BaseActivity.Animation(BaseActivity.MODAL)
class AmountSelectorActivity: BaseActivity() {

    companion object {
        const val PAYMENT_FINISHED = 0
    }

    /* Activity methods */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amount_selector)
        setUpViews()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SummaryActivity.PAYMENT_FINISHED) {
            if (resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    /* setUp methods */

    private fun setUpViews() {
        stepperButton.isEnabled = false
        amountEditText.filters = arrayOf<InputFilter>(InputFilterMinMax("0.0", "1000.0"))
        amountEditText.afterTextChange {
            stepperButton.isEnabled = it.isNotEmpty()
        }
        stepperButton.setTitle(R.string.amount_selector_summary)
        stepperButton.setOnClickListener {
            val floatAmount = amountEditText.text.toString().toFloat()
            val intent = Intent(this, SummaryActivity::class.java)
            intent.putExtra(SummaryActivity.EXTRA_AMOUNT, floatAmount)
            startActivityForResult(intent, SummaryActivity.PAYMENT_FINISHED)
        }
    }

}
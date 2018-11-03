package com.vsantander.paymentchallenge.presentation.amountselector

import android.os.Bundle
import com.vsantander.paymentchallenge.R
import com.vsantander.paymentchallenge.presentation.base.activity.BaseActivity
import com.vsantander.paymentchallenge.presentation.summary.SummaryActivity
import com.vsantander.paymentchallenge.utils.extension.afterTextChange
import kotlinx.android.synthetic.main.activity_amount_selector.*
import org.jetbrains.anko.startActivity
import com.vsantander.paymentchallenge.presentation.view.InputFilterMinMax
import android.text.InputFilter



@BaseActivity.Animation(BaseActivity.MODAL)
class AmountSelectorActivity: BaseActivity() {

    /* Activity methods */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amount_selector)
        setUpViews()
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
            startActivity<SummaryActivity>(SummaryActivity.EXTRA_AMOUNT to floatAmount)
            finish()
        }
    }

}
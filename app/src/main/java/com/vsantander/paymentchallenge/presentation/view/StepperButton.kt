package com.vsantander.paymentchallenge.presentation.view

import android.content.Context
import android.support.annotation.StringRes
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.vsantander.paymentchallenge.R
import kotlinx.android.synthetic.main.view_stepper_button.view.*
import org.jetbrains.anko.dimen

class StepperButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    init {
        RelativeLayout.inflate(context, R.layout.view_stepper_button, this)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                dimen(R.dimen.contact_view_stepper_height))
    }

    fun setTitle(value: String) {
        stepperButtonTextView.text = value
    }

    fun setTitle(@StringRes value: Int) {
        setTitle(context.getString(value))
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        stepperButtonTextView.isEnabled = enabled
        stepperButtonImageView.isEnabled = enabled

        if (enabled) {
            alpha = .8f
            stepperButtonTextView.alpha = 1f
            stepperButtonImageView.alpha = 1f
        } else {
            alpha = 1f
            stepperButtonTextView.alpha = .2f
            stepperButtonImageView.alpha = .2f
        }

    }
}
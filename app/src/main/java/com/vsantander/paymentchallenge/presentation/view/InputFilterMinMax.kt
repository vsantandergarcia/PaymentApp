package com.vsantander.paymentchallenge.presentation.view

import android.text.Spanned
import android.text.InputFilter
import timber.log.Timber

class InputFilterMinMax constructor(private val min: String, private val max: String) : InputFilter {

    override fun filter(source: CharSequence, start: Int, end: Int,
                        dest: Spanned, dstart: Int, dend: Int): CharSequence? {
        try {
            val input = (dest.toString() + source.toString()).toFloat()
            if (isInRange(min.toFloat(), max.toFloat(), input)) {
                return null
            }
        } catch (e: NumberFormatException) {
            Timber.e(e)
        }

        return ""
    }

    private fun isInRange(a: Float, b: Float, c: Float): Boolean {
        return if (b > a) {
            c in a..b
        } else {
            c in b..a
        }
    }
}
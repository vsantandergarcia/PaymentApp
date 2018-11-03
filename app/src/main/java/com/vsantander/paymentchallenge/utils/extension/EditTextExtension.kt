package com.vsantander.paymentchallenge.utils.extension

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

inline fun EditText.afterTextChange(crossinline call: (String) -> Unit) {
    val view = this
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            // purposefully not implemented.
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            // purposefully not implemented.
        }

        override fun afterTextChanged(s: Editable) {
            if (view.hasFocus()) {
                call(s.toString())
            }
        }
    })
}
package com.vsantander.paymentchallenge.presentation.base.item

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet

abstract class ItemView<T> @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    abstract fun bind(item: T)

}
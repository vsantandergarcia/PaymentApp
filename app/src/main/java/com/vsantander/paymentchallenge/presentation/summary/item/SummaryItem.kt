package com.vsantander.paymentchallenge.presentation.summary.item

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.vsantander.paymentchallenge.R
import com.vsantander.paymentchallenge.domain.model.Contact
import com.vsantander.paymentchallenge.presentation.base.item.ItemView
import com.vsantander.paymentchallenge.utils.Constants
import kotlinx.android.synthetic.main.view_item_summary.view.*
import org.jetbrains.anko.dimen

class SummaryItem @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ItemView<Contact>(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_item_summary, this, true)
        useCompatPadding = true
        radius = context.dimen(R.dimen.contact_item_radius).toFloat()
        cardElevation = context.dimen(R.dimen.contact_item_elevation).toFloat()
    }

    override fun bind(item: Contact) {
        contactNameTextView.text = item.name
        item.phone?.let {
            phoneTextView.text = it
        }

        Glide
                .with(context)
                .load(if (item.avatar.isNullOrBlank()) R.drawable.no_picture else item.avatar)
                .transition(DrawableTransitionOptions.withCrossFade(Constants.DURATION_FADE_GLIDE))
                .into(contactImageView)
    }

    fun bindAmount(amount: Float) {
        val res = "%.2f".format(amount) + " " + context.getString(R.string.summary_money)
        amountTextView.text = res
    }
}
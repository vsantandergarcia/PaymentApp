package com.vsantander.paymentchallenge.presentation.summary.adapter

import android.view.ViewGroup
import com.vsantander.paymentchallenge.domain.model.Contact
import com.vsantander.paymentchallenge.presentation.base.adapter.RecyclerViewAdapterBase
import com.vsantander.paymentchallenge.presentation.base.adapter.ViewWrapper
import com.vsantander.paymentchallenge.presentation.base.item.ItemView
import com.vsantander.paymentchallenge.presentation.summary.item.SummaryItem

class SummaryListAdapter : RecyclerViewAdapterBase<Contact, ItemView<Contact>>() {

    override fun onCreateItemView(parent: ViewGroup, viewType: Int): ItemView<Contact> {
        return SummaryItem(parent.context)
                .apply {
                    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT)
                }
    }

    override fun onBindViewHolder(holder: ViewWrapper<ItemView<Contact>>, position: Int) {
        val item = items[position]

        holder.view.apply {
            bind(item)
        }
    }
}
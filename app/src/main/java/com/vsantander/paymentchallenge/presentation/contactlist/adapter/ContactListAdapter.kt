package com.vsantander.paymentchallenge.presentation.contactlist.adapter

import android.view.ViewGroup
import com.vsantander.paymentchallenge.domain.model.Contact
import com.vsantander.paymentchallenge.presentation.base.adapter.RecyclerViewAdapterBase
import com.vsantander.paymentchallenge.presentation.base.adapter.ViewWrapper
import com.vsantander.paymentchallenge.presentation.base.item.ItemView
import com.vsantander.paymentchallenge.presentation.contactlist.item.ContactItem

class ContactListAdapter : RecyclerViewAdapterBase<Contact, ItemView<Contact>>() {

    var onClickAction: ((item: Contact) -> Unit)? = null

    var isClickable = false

    override fun onCreateItemView(parent: ViewGroup, viewType: Int): ItemView<Contact> {
        return ContactItem(parent.context)
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

        holder.view.setOnClickListener {
            if (isClickable) {
                onClickAction?.invoke(items[position])
            }
        }
    }
}
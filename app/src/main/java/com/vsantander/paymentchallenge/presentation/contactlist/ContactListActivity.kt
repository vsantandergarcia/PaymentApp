package com.vsantander.paymentchallenge.presentation.contactlist

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.vsantander.paymentchallenge.R
import com.vsantander.paymentchallenge.presentation.base.activity.BaseActivity
import com.vsantander.paymentchallenge.presentation.contactlist.adapter.ContactListAdapter
import com.vsantander.paymentchallenge.presentation.model.Status
import com.vsantander.paymentchallenge.utils.extension.observe
import kotlinx.android.synthetic.main.activity_contact_list.*
import timber.log.Timber
import javax.inject.Inject

@BaseActivity.Animation(BaseActivity.FADE)
class ContactListActivity: BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ContactListViewModel

    private lateinit var adapter: ContactListAdapter

    /* Activity methods */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)
        setUpViews()
        setUpViewModels()
    }

    /* setUp methods */

    private fun setUpViews() {
        setUpToolbar()
        adapter = ContactListAdapter().apply {
            onClickAction = {
                Timber.d("item client click with name:${it.name}")
            }
        }

        swipeRefreshLayout.setOnRefreshListener { viewModel.loadInfo() }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context,
                    LinearLayoutManager.VERTICAL, false) as RecyclerView.LayoutManager
            adapter = this@ContactListActivity.adapter
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun setUpViewModels() {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ContactListViewModel::class.java)


        viewModel.resource.observe(this) { resource ->
            resource ?: return@observe

            swipeRefreshLayout.isRefreshing = resource == Status.LOADING

            if (resource.status == Status.SUCCESS) {
                adapter.setItems(resource.data!!)
                adapter.isClickable = true
            } else if (resource.status == Status.FAILED) {
                Snackbar.make(recyclerView, R.string.common_error, Snackbar.LENGTH_LONG)
                        .setAction(R.string.retry) { viewModel.loadInfo() }
                        .show()
            }
        }

        viewModel.loadInfo()
    }

}
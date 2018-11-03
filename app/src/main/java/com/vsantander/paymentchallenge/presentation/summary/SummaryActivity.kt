package com.vsantander.paymentchallenge.presentation.summary

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import androidx.core.view.isVisible
import com.vsantander.paymentchallenge.R
import com.vsantander.paymentchallenge.presentation.base.activity.BaseActivity
import com.vsantander.paymentchallenge.presentation.model.Status
import com.vsantander.paymentchallenge.presentation.summary.adapter.SummaryListAdapter
import com.vsantander.paymentchallenge.utils.extension.observe
import kotlinx.android.synthetic.main.activity_summary.*
import javax.inject.Inject

@BaseActivity.Animation(BaseActivity.MODAL)
class SummaryActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: SummaryViewModel

    private lateinit var adapter: SummaryListAdapter

    /* Activity methods */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)
        setUpViews()
        setUpViewModel()
    }

    /* setUp methods */

    private fun setUpViews() {
        setUpToolbar()
        adapter = SummaryListAdapter()

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context,
                    LinearLayoutManager.VERTICAL, false) as RecyclerView.LayoutManager
            adapter = this@SummaryActivity.adapter
        }

        stepperButton.setTitle(R.string.summary_buy)
        stepperButton.setOnClickListener {
            //TODO
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(SummaryViewModel::class.java)

        viewModel.resource.observe(this) { resource ->
            resource ?: return@observe

            progressBar.isVisible = resource == Status.LOADING

            if (resource.status == Status.SUCCESS) {
                adapter.setItems(resource.data!!)
            } else if (resource.status == Status.FAILED) {
                Snackbar.make(recyclerView, R.string.common_error, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.retry) { viewModel.loadSelectedContacts() }
                        .show()
            }
        }

    }
}
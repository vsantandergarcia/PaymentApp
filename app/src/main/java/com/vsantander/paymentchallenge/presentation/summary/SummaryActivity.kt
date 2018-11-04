package com.vsantander.paymentchallenge.presentation.summary

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
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
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@BaseActivity.Animation(BaseActivity.FADE)
class SummaryActivity : BaseActivity() {

    companion object {
        const val EXTRA_AMOUNT = "EXTRA_AMOUNT"
        private val SUCCESS_DELAY = TimeUnit.SECONDS.toMillis(2) // 2 seconds
        const val PAYMENT_FINISHED = 0
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: SummaryViewModel

    private lateinit var adapter: SummaryListAdapter

    private val handler = Handler()

    private val runnableFinishPayment: Runnable = Runnable {
        if (!isFinishing) {
            finish()
        }
    }

    /* Activity methods */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        intent?.extras?.getFloat(EXTRA_AMOUNT)?.let {
            setUpViews(it)
            setUpViewModel()
        } ?: throw RuntimeException("bad initialization. not found some extras")
    }

    public override fun onDestroy() {
        handler.removeCallbacks(runnableFinishPayment)
        super.onDestroy()
    }

    /* setUp methods */

    private fun setUpViews(amount: Float) {
        setUpToolbar()
        adapter = SummaryListAdapter(amount)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context,
                    LinearLayoutManager.VERTICAL, false) as RecyclerView.LayoutManager
            adapter = this@SummaryActivity.adapter
        }

        stepperButton.setTitle(R.string.summary_buy)
        stepperButton.setOnClickListener {
            viewModel.performFakePayment()
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

            progressBar.isVisible = resource.status == Status.LOADING

            if (resource.status == Status.SUCCESS) {
                adapter.setItems(resource.data!!)
            } else if (resource.status == Status.FAILED) {
                Snackbar.make(recyclerView, R.string.common_error, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.retry) { viewModel.loadSelectedContacts() }
                        .show()
                stepperButton.isEnabled = true
            }
        }

        viewModel.paymentProcess.observe(this) { paymentProcess ->
            paymentProcess ?: return@observe

            progressBar.isVisible = paymentProcess.status == Status.LOADING
            stepperButton.isEnabled = paymentProcess.status != Status.FAILED

            if (paymentProcess.status == Status.SUCCESS) {
                setResult(Activity.RESULT_OK)
                Snackbar.make(recyclerView, R.string.summary_payment_ok, Snackbar.LENGTH_SHORT)
                        .show()
                handler.postDelayed(runnableFinishPayment, SUCCESS_DELAY)
            } else if (paymentProcess.status == Status.FAILED) {
                Snackbar.make(recyclerView, R.string.summary_payment_error, Snackbar.LENGTH_SHORT)
                        .show()
            }
        }

    }
}
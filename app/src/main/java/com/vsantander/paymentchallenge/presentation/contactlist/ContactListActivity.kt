package com.vsantander.paymentchallenge.presentation.contactlist

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.widget.LinearLayout
import com.vsantander.paymentchallenge.R
import com.vsantander.paymentchallenge.presentation.base.activity.BaseActivity
import com.vsantander.paymentchallenge.presentation.contactlist.adapter.ContactListAdapter
import com.vsantander.paymentchallenge.presentation.model.Status
import com.vsantander.paymentchallenge.utils.extension.observe
import kotlinx.android.synthetic.main.activity_contact_list.*
import timber.log.Timber
import javax.inject.Inject
import pub.devrel.easypermissions.EasyPermissions
import androidx.core.view.isVisible
import com.vsantander.paymentchallenge.presentation.amountselector.AmountSelectorActivity
import com.vsantander.paymentchallenge.utils.Constants
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import com.vsantander.paymentchallenge.presentation.view.SmoothScrollLinearLayoutManager


@BaseActivity.Animation(BaseActivity.FADE)
class ContactListActivity : BaseActivity(), EasyPermissions.PermissionCallbacks {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ContactListViewModel

    private lateinit var adapter: ContactListAdapter

    /* Activity methods */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)
        setUpViews()
        setUpViewModel()
        checkPermissions()
        viewModel.loadContacts()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AmountSelectorActivity.PAYMENT_FINISHED) {
            if (resultCode == Activity.RESULT_OK) {
                viewModel.loadContacts()
            }
        }
    }

    /* setUp methods */

    private fun setUpViews() {
        setUpToolbar()
        adapter = ContactListAdapter().apply {
            onClickSelectorAction = {
                Timber.d("item selector click with name:${it.name}")
                if (it.isSelected) {
                    viewModel.saveSelectedContact(it)
                } else {
                    viewModel.deleteSelectedContact(it)
                }
            }
        }

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = true
            viewModel.loadContacts()
        }
        recyclerView.apply {
            layoutManager = SmoothScrollLinearLayoutManager(context,
                    LinearLayout.VERTICAL, false)
            adapter = this@ContactListActivity.adapter
        }

        arrowUpButton.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
        }
        arrowDownButton.setOnClickListener {
            recyclerView.smoothScrollToPosition(recyclerView.adapter!!.itemCount-1)
        }

        stepperButton.setTitle(R.string.contact_list_select_amount)
        stepperButton.setOnClickListener {
            val intent = Intent(this, AmountSelectorActivity::class.java)
            startActivityForResult(intent, AmountSelectorActivity.PAYMENT_FINISHED)
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ContactListViewModel::class.java)

        viewModel.resource.observe(this) { resource ->
            resource ?: return@observe

            progressBar.isVisible = resource.status == Status.LOADING

            if (resource.status == Status.SUCCESS) {
                swipeRefreshLayout.isRefreshing = false
                adapter.setItems(resource.data!!)
            } else if (resource.status == Status.FAILED) {
                swipeRefreshLayout.isRefreshing = false
                Snackbar.make(recyclerView, resource.msg ?: getString(R.string.common_error),
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.retry) { viewModel.loadContacts() }
                        .show()
            }
        }

        viewModel.currentNumberSelectedContacts.observe(this) { numberSelectedContacts ->
            numberSelectedContacts ?: return@observe
            stepperButton.isEnabled = numberSelectedContacts > 0
        }
    }

    /* EasyPermissions.PermissionCallbacks methods */

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Timber.d("onPermissionsDenied:%s", requestCode)
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Timber.d("onPermissionsGranted:%s", requestCode)
        viewModel.readContactsPermissionAccepted = true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            viewModel.readContactsPermissionAccepted = true
            viewModel.loadContacts()
        }
    }

    @AfterPermissionGranted(Constants.READ_CONTACTS_PERMISSION)
    private fun checkPermissions() {
        val permission = Manifest.permission.READ_CONTACTS
        if (EasyPermissions.hasPermissions(this, permission)) {
            viewModel.readContactsPermissionAccepted = true
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.permission_read_contacts),
                    Constants.READ_CONTACTS_PERMISSION, permission)
        }
    }

}
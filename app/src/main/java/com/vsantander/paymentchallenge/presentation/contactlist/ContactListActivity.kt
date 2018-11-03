package com.vsantander.paymentchallenge.presentation.contactlist

import android.Manifest
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
import pub.devrel.easypermissions.EasyPermissions
import androidx.core.view.isVisible
import com.vsantander.paymentchallenge.R.id.swipeRefreshLayout
import com.vsantander.paymentchallenge.presentation.amountselector.AmountSelectorActivity
import com.vsantander.paymentchallenge.utils.Constants
import org.jetbrains.anko.startActivity
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog



@BaseActivity.Animation(BaseActivity.FADE)
class ContactListActivity: BaseActivity(), EasyPermissions.PermissionCallbacks{

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

        swipeRefreshLayout.setOnRefreshListener { viewModel.loadContacts() }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context,
                    LinearLayoutManager.VERTICAL, false) as RecyclerView.LayoutManager
            adapter = this@ContactListActivity.adapter
        }

        stepperButton.setTitle(R.string.contact_list_select_amount)
        stepperButton.setOnClickListener {
            startActivity<AmountSelectorActivity>()
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

            swipeRefreshLayout.isRefreshing = resource == Status.LOADING
            progressBar.isVisible = resource == Status.LOADING

            if (resource.status == Status.SUCCESS) {
                adapter.setItems(resource.data!!)
            } else if (resource.status == Status.FAILED) {
                Snackbar.make(recyclerView, R.string.common_error, Snackbar.LENGTH_INDEFINITE)
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

        viewModel.readContactsPermissionAccepted = true
        viewModel.loadContacts()
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
package com.abhrp.foodnearme.ui.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.abhrp.foodnearme.R
import com.abhrp.foodnearme.util.ConnectionMonitor
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerAppCompatActivity
import permissions.dispatcher.PermissionRequest
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var connectionMonitor: ConnectionMonitor

    private var offLineSnackbar: Snackbar? = null
    var isOnline: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observerConnectionStatus()
    }

    private fun observerConnectionStatus() {
        connectionMonitor.observe(this, Observer {
            isOnline = it
            if (it) {
                online()
            } else {
                offline()
            }
        })
    }

    fun showOffLineSnackBar(view: View) {
        offLineSnackbar =
            Snackbar.make(view, getString(R.string.no_internet_message), Snackbar.LENGTH_INDEFINITE)
        offLineSnackbar?.setAction(getString(R.string.action_dismiss)) {
            offLineSnackbar?.dismiss()
        }
        offLineSnackbar?.show()
    }

    fun dismissOfflineSnackBar() {
        offLineSnackbar?.dismiss()
    }

    fun showError(message: String?) {
        Toast.makeText(this, message ?: getString(R.string.generic_error), Toast.LENGTH_LONG).show()
    }

    fun showToast(@StringRes messageId: Int) {
        Toast.makeText(this, getString(messageId), Toast.LENGTH_LONG).show()
    }

    fun showRationaleDialog(@StringRes messageResId: Int, request: PermissionRequest) {
        AlertDialog.Builder(this)
            .setPositiveButton(R.string.button_allow) { _, _ -> request.proceed() }
            .setNegativeButton(R.string.button_deny) { _, _ -> request.cancel() }
            .setCancelable(false)
            .setMessage(messageResId)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissOfflineSnackBar()
    }

    override fun onPause() {
        super.onPause()
        dismissOfflineSnackBar()
    }

    abstract fun online()
    abstract fun offline()

}
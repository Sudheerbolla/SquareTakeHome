package com.squaretakehome.utils

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.squaretakehome.R

object StaticUtils {
    //            while loading
    val VIEW_LOADING = 0
    //            result successful
    val VIEW_SUCCESS = 1
    //            result failure
    val VIEW_ERROR = 2
    var softKeyBarHeight = 100

    fun checkInternetConnection(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // if the android version is equal to M or greater we need to use the NetworkCapabilities to
        // check what type of network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    fun showSimpleToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun showSimpleToast(context: Context, message: Int) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun showToast(container: View, message: String) {
        try {
            val snackbar = snackbar(container, message, Snackbar.LENGTH_LONG)
            snackbar.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Snackbar toast which will be auto dismissed and action
     */
    fun showToast(
        container: View,
        message: String,
        actionText: String,
        onClickListener: View.OnClickListener
    ) {
        try {
            val snackbar = snackbar(container, message, Snackbar.LENGTH_LONG)
            snackbar.setAction(actionText, onClickListener)
            snackbar.setActionTextColor(Color.BLACK)
            snackbar.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Showing snackbar with action. Can be only dismissed by action
     */
    fun showIndefiniteToast(
        container: View,
        message: String,
        actionText: String,
        onClickListener: View.OnClickListener
    ) {
        try {
            val snackbar = snackbar(container, message, Snackbar.LENGTH_INDEFINITE)
            snackbar.setAction(actionText, onClickListener)
            snackbar.setActionTextColor(Color.RED)
            snackbar.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun snackbar(container: View, message: String, duration: Int): Snackbar {
        val snackbar = Snackbar.make(container, message, duration)
        try {
            val sbView = snackbar.view
            val params: ViewGroup.LayoutParams
            params = sbView.getLayoutParams() as FrameLayout.LayoutParams
            params.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
            params.setMargins(0, 0, 0, softKeyBarHeight)
            sbView.setLayoutParams(params)
            sbView.setBackgroundColor(ContextCompat.getColor(container.context, R.color.colorGrey))
            val textView =
                sbView.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
            textView.setTextColor(Color.WHITE)
            textView.maxLines = 5
            textView.gravity = Gravity.CENTER_HORIZONTAL
            snackbar.addCallback(object : Snackbar.Callback() {

                override fun onDismissed(snackbar: Snackbar?, event: Int) {
                    if (event == DISMISS_EVENT_TIMEOUT) {
                        // Snackbar closed on its own
                    }
                }

                override fun onShown(snackbar: Snackbar?) {}
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return snackbar
    }

}
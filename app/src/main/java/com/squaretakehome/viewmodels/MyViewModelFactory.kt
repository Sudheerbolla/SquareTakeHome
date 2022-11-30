package com.squaretakehome.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MyViewModelFactory(
    private val application: Application
) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(EmployeesViewModel::class.java)) {
            EmployeesViewModel(application) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}
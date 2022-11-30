package com.squaretakehome.viewmodels

import android.app.Application
import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.squaretakehome.BaseApplication
import com.squaretakehome.R
import com.squaretakehome.models.EmployeesRootModel
import com.squaretakehome.repository.EmployeeRepositoryImpl
import com.squaretakehome.utils.StaticUtils
import com.squaretakehome.utils.wsutils.EmployeeApi
import com.squaretakehome.utils.wsutils.RetrofitHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EmployeesViewModel(application: Application) : AndroidViewModel(application) {

    var employeesLiveData = MutableLiveData<EmployeesRootModel>()
    val apiError = MutableLiveData<String>()

    //  fields for maintaining Visibility
    var loading: ObservableInt? = ObservableInt(View.VISIBLE)
    var showEmpty: ObservableInt? = ObservableInt(View.GONE)
    var showData: ObservableInt? = ObservableInt(View.GONE)

    private val baseApplication: BaseApplication
    private val retrofitInstance: EmployeeApi
    private val repository: EmployeeRepositoryImpl

    init {
        this.baseApplication = application as BaseApplication
        retrofitInstance = RetrofitHelper.getRetrofitInstance()
        repository = EmployeeRepositoryImpl(retrofitInstance)
        requestDataFromServer()
    }

    fun requestDataFromServer() {
        updateVisibilities(StaticUtils.VIEW_LOADING)
        MainScope().launch {
            try {
                fetchEmployees()
            } catch (exception: Exception) {
                apiError.value =
                    baseApplication.getString(R.string.error_getting_data_from_api)
                updateVisibilities(StaticUtils.VIEW_ERROR)
            }
        }
    }

    /**
     * Api call for getting Employees
     */
    private suspend fun fetchEmployees() = withContext(Dispatchers.Main) {
        try {
            repository.getAllEmployees().body?.let {
                if (it.employeesList.isEmpty()) {
                    apiError.value = "Zero Records came from server"
                    updateVisibilities(StaticUtils.VIEW_ERROR)
                } else {
                    employeesLiveData.value = it
                    updateVisibilities(StaticUtils.VIEW_SUCCESS)
                }
            } ?: run {
                apiError.value =
                    baseApplication.getString(R.string.error_getting_data_from_api)
                updateVisibilities(StaticUtils.VIEW_ERROR)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            apiError.value = baseApplication.getString(R.string.error_getting_data_from_api)
            updateVisibilities(StaticUtils.VIEW_ERROR)
        }
    }

    //Handling visibilities of the main screen
    fun updateVisibilities(showDataViews: Int) {
        when (showDataViews) {
            StaticUtils.VIEW_LOADING -> {
                loading?.set(View.VISIBLE)
                showEmpty?.set(View.GONE)
                showData?.set(View.GONE)
            }
            StaticUtils.VIEW_SUCCESS -> {
                loading?.set(View.GONE)
                showEmpty?.set(View.GONE)
                showData?.set(View.VISIBLE)
            }
            StaticUtils.VIEW_ERROR -> {
                loading?.set(View.GONE)
                showEmpty?.set(View.VISIBLE)
                showData?.set(View.GONE)
            }
        }
    }

}
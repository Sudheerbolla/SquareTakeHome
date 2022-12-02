package com.squaretakehome.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.squaretakehome.R
import com.squaretakehome.adapters.EmployeesAdapter
import com.squaretakehome.databinding.ActivityHomeBinding
import com.squaretakehome.repository.EmployeeRepository
import com.squaretakehome.utils.StaticUtils
import com.squaretakehome.viewmodels.EmployeesViewModel
import com.squaretakehome.viewmodels.MyViewModelFactory

class HomeActivity : AppCompatActivity() {
    private val TAG = "HomeActivity"

    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: EmployeesViewModel
    private lateinit var adapter: EmployeesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null || binding == null) {
            binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
            binding.lifecycleOwner = this
            initComponents()
        }
    }

    private fun initComponents() {
        viewModel =
            ViewModelProvider(
                this,
                MyViewModelFactory(application)
            )[EmployeesViewModel::class.java]
        binding.viewModel = viewModel
        setUpRecyclerView()
        setViewModelObservers()
        binding.swipeToRefreshLayout.setOnRefreshListener {
            viewModel.requestDataFromServer()
        }
    }

    private fun setUpRecyclerView() {
        adapter = EmployeesAdapter()
        binding.recyclerViewEmployees.adapter = adapter
    }

    private fun setViewModelObservers() {
        viewModel.employeesLiveData.observe(this) {
            if (binding.swipeToRefreshLayout.isRefreshing)
                binding.swipeToRefreshLayout.isRefreshing = false
            adapter.updateList(it.employeesList)
        }
        viewModel.apiError.observe(this) { error ->
            binding.txtNoData.text = error
            StaticUtils.showIndefiniteToast(
                binding.root,
                error,
                getString(R.string.retry)
            ) {
                viewModel.requestDataFromServer()
            }
        }
    }

}

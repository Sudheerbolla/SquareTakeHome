package com.squaretakehome.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.squaretakehome.BR
import com.squaretakehome.R
import com.squaretakehome.databinding.EmployeeItemBinding
import com.squaretakehome.models.EmployeeModel

class EmployeesAdapter : RecyclerView.Adapter<MainViewHolder>() {

    var employeeModelArrayList: ArrayList<EmployeeModel>

    init {
        employeeModelArrayList = ArrayList()
    }

    fun updateList(employeeModelArrayList: List<EmployeeModel>?) {
        this.employeeModelArrayList = ArrayList(employeeModelArrayList!!.sortedBy {
            it.fullName
        })
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.employee_item, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(employeeModelArrayList.get(position))
    }

    override fun getItemCount(): Int {
        return employeeModelArrayList.size
    }
}

class MainViewHolder(private val binding: EmployeeItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(employeeModel: EmployeeModel) {
        binding.setVariable(BR.employeeModel, employeeModel)
        binding.executePendingBindings()
    }

}
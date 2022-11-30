package com.squaretakehome.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EmployeesRootModel(
    @Json(name = "employees") var employeesList: List<EmployeeModel> = arrayListOf()
)
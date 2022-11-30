package com.squaretakehome.repository

import com.squaretakehome.utils.wsutils.ApiResponse
import com.squaretakehome.models.EmployeesRootModel

interface EmployeeRepository {

    suspend fun getAllEmployees(): ApiResponse<EmployeesRootModel>

    suspend fun getMalformedEmployees(): ApiResponse<EmployeesRootModel>

    suspend fun getEmptyEmployees(): ApiResponse<EmployeesRootModel>
}
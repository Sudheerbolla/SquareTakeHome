package com.squaretakehome.repository

import com.squaretakehome.utils.wsutils.ApiResponse
import com.squaretakehome.utils.wsutils.EmployeeApi
import com.squaretakehome.models.EmployeesRootModel
import retrofit2.HttpException
import java.io.IOException

class EmployeeRepositoryImpl(
    private val employeeApi: EmployeeApi
): EmployeeRepository {

    override suspend fun getAllEmployees(): ApiResponse<EmployeesRootModel> {
        return try {
            ApiResponse(
                body = employeeApi.getAllEmployees()
            )
        } catch (e: HttpException) {
            ApiResponse(
                httpCode = e.code(),
                errorMessage = e.message()
            )
        } catch (e: IOException) {
            ApiResponse(
                errorMessage = e.message
            )
        }
    }

    override suspend fun getMalformedEmployees(): ApiResponse<EmployeesRootModel> {
        return try {
            ApiResponse(
                body = employeeApi.getMalformedEmployees()
            )
        } catch (e: HttpException) {
            ApiResponse(
                httpCode = e.code(),
                errorMessage = e.message()
            )
        } catch (e: IOException) {
            ApiResponse(
                errorMessage = e.message
            )
        }
    }

    override suspend fun getEmptyEmployees(): ApiResponse<EmployeesRootModel> {
        return try {
            ApiResponse(
                body = employeeApi.getEmptyEmployees()
            )
        } catch (e: HttpException) {
            ApiResponse(
                httpCode = e.code(),
                errorMessage = e.message()
            )
        } catch (e: IOException) {
            ApiResponse(
                errorMessage = e.message
            )
        }
    }
}
package com.squaretakehome.utils.wsutils

import com.squaretakehome.BuildConfig
import com.squaretakehome.models.EmployeesRootModel
import retrofit2.http.GET
import retrofit2.http.Headers

interface EmployeeApi {

    @Headers("Content-Type: application/json")
    @GET(BuildConfig.SUCCESS_URL)
    suspend fun getAllEmployees(): EmployeesRootModel

    @Headers("Content-Type: application/json")
    @GET(BuildConfig.MALFORMED_URL)
    suspend fun getMalformedEmployees(): EmployeesRootModel

    @Headers("Content-Type: application/json")
    @GET(BuildConfig.EMPTY_URL)
    suspend fun getEmptyEmployees(): EmployeesRootModel

}

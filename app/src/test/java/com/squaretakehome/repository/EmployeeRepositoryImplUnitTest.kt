package com.squaretakehome.repository

import com.squaretakehome.models.EmployeeModel
import com.squaretakehome.utils.wsutils.EmployeeApi
import com.squaretakehome.utils.wsutils.RetrofitHelper
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import java.net.HttpURLConnection

class EmployeeRepositoryImplUnitTest {

    private lateinit var repository: EmployeeRepository
    private lateinit var testApis: EmployeeApi
    private lateinit var mockWebServer: MockWebServer

    private val createEmployee = createEmployee()
    private val createEmployee2 = createEmployee(employeeType = "PART_TIME")

    private lateinit var adapter: JsonAdapter<List<EmployeeModel>>
    lateinit var mosh: Moshi

    @Before
    fun setUp() {
        mosh = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        mockWebServer = MockWebServer()
        mockWebServer.start()
        testApis = RetrofitHelper.getRetrofitInstance()
        repository = EmployeeRepositoryImpl(testApis)
        adapter =
            mosh.adapter(Types.newParameterizedType(List::class.java, EmployeeModel::class.java))
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    // emptyUrl
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun shouldReturnEmptyEmployeesWhenWeCallEmptyEmployeeURL() = runTest {
        val users = emptyList<EmployeeModel>()

        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(adapter.toJson(users))
        mockWebServer.enqueue(expectedResponse)

        val actualResponse = repository.getEmptyEmployees()

        assertThat(actualResponse.body?.employeesList?.isEmpty(), `is`(true))
    }

    // Malformed
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun shouldReturnMultipleMalformedEmployeesWhenWeCallMalformedURL() = runTest {
        val employees = arrayListOf(createEmployee, createEmployee2)

        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(adapter.toJson(employees))
        mockWebServer.enqueue(expectedResponse)

        val actualResponse = repository.getMalformedEmployees()

        assertThat(actualResponse.body?.employeesList?.count(), `is`(11))
        assertThat(actualResponse.body?.employeesList?.get(9)?.team, `is`("Hardware"))
        assertThat(actualResponse.body?.employeesList?.get(9)?.uuid, `is`(nullValue()))
        assertThat(actualResponse.body?.employeesList?.get(1)?.team, `is`(nullValue()))
        assertThat(actualResponse.body?.employeesList?.get(10)?.biography, `is`("I work for you."))
        assertThat(
            actualResponse.body?.employeesList?.first()?.fullName,
            `is`(createEmployee.fullName)
        )
        assertThat(
            actualResponse.body?.employeesList?.first()?.phoneNumber,
            `is`(createEmployee.phoneNumber)
        )
        assertThat(
            actualResponse.body?.employeesList?.first()?.emailAddress,
            `is`(createEmployee.emailAddress)
        )
        assertThat(
            actualResponse.body?.employeesList?.first()?.biography,
            `is`(createEmployee.biography)
        )
    }

    // Employee
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun shouldReturnMultipleEmployeesWhenWeCallEmployeesURL() = runTest {
        val employees = arrayListOf(createEmployee, createEmployee2)

        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(adapter.toJson(employees))
        mockWebServer.enqueue(expectedResponse)

        val actualResponse = repository.getAllEmployees()

        assertThat(actualResponse.body?.employeesList?.count(), `is`(11))
        assertThat(
            actualResponse.body?.employeesList?.get(0)?.employeeType,
            `is`(createEmployee.employeeType)
        )
        assertThat(actualResponse.body?.employeesList?.get(1)?.team, `is`(notNullValue()))
        assertThat(actualResponse.body?.employeesList?.get(9)?.uuid, `is`(notNullValue()))
        assertThat(
            actualResponse.body?.employeesList?.first()?.fullName,
            `is`(createEmployee.fullName)
        )
        assertThat(
            actualResponse.body?.employeesList?.first()?.phoneNumber,
            `is`(createEmployee.phoneNumber)
        )
        assertThat(
            actualResponse.body?.employeesList?.first()?.emailAddress,
            `is`(createEmployee.emailAddress)
        )
        assertThat(
            actualResponse.body?.employeesList?.first()?.biography,
            `is`(createEmployee.biography)
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Ignore //This case always returns never executed
    @Test
    fun `for server error, api must return with http code 5xx and error message`() = runTest {
        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
        mockWebServer.enqueue(expectedResponse)

        val actualResponse = repository.getAllEmployees()
        assertThat(actualResponse.httpCode, `is`(HttpURLConnection.HTTP_INTERNAL_ERROR))
        assertThat(actualResponse.errorMessage, `is`("server error"))
    }

    private fun createEmployee(
        uuid: String = "",
        fullName: String = "Justine Mason",
        phoneNumber: String = "5553280123",
        emailAddress: String = "jmason.demo@squareup.com",
        biography: String = "Engineer on the Point of Sale team.",
        photoUrlSmall: String = "",
        photoUrlLarge: String = "",
        team: String = "Point of Sale",
        employeeType: String = "FULL_TIME"
    ) = EmployeeModel(
        uuid,
        fullName,
        phoneNumber,
        emailAddress,
        biography,
        photoUrlSmall,
        photoUrlLarge,
        team,
        employeeType
    )
}
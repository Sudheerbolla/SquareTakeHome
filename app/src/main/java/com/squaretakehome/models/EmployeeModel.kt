package com.squaretakehome.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EmployeeModel(
    @Json(name = "uuid") val uuid: String,
    @Json(name = "full_name") val fullName: String,
    @Json(name = "phone_number") val phoneNumber: String,
    @Json(name = "email_address") val emailAddress: String,
    @Json(name = "biography") val biography: String,
    @Json(name = "photo_url_small") val photoUrlSmall: String,
    @Json(name = "photo_url_large") val photoUrlLarge: String,
    @Json(name = "team") val team: String,
    @Json(name = "employee_type") val employeeType: String
)

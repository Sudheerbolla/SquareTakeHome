package com.squaretakehome.utils.wsutils

import java.net.HttpURLConnection

data class ApiResponse<T>(
    val httpCode: Int = HttpURLConnection.HTTP_OK,
    val body: T? = null,
    val errorMessage: String? = null,
)